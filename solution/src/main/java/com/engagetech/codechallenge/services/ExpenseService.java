package com.engagetech.codechallenge.services;

import com.engagetech.codechallenge.data.dto.ExpenseCreateProjectionDto;
import com.engagetech.codechallenge.data.dto.ExpenseWithCurrencyCreateDto;
import com.engagetech.codechallenge.data.model.Expense;
import com.engagetech.codechallenge.data.projection.ExpenseIdLessProjection;
import com.engagetech.codechallenge.data.repository.ExpenseRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * Expense service provides basic operation for user, so user can operates only his/her expenses.
 */
@Service
public class ExpenseService {

    private final ModelMapper modelMapper;

    private final ExpenseRepository expenseRepository;

    private final AuditorAware<String> auditorAware;

    @Autowired
    ExpenseService(ModelMapper modelMapper, ExpenseRepository expenseRepository, AuditorAware<String> auditorAware) {
        this.modelMapper = modelMapper;
        this.expenseRepository = expenseRepository;
        this.auditorAware = auditorAware;
    }

    /**
     * Get all user's expenses
     * @return list of user's expenses
     */
    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    /**
     * Get all user's expenses transformed according to T-projection
     * @return list of user's expenses
     */
    public <T> List<T> getExpenses(Class<T> type) {
        String currentUserId = auditorAware.getCurrentAuditor().get();
        return expenseRepository.findByCreatedBy(currentUserId, type);
    }

    /**
     * Creates projection and assign it to current user
     * @param expenseCreateProjection dto which contains all data for creating exppense
     * @return created expense
     */
    public Expense createExpenseWithoutRate(ExpenseIdLessProjection expenseCreateProjection) {

        Expense expense = modelMapper.map(expenseCreateProjection, Expense.class);

        // Workaround for a problem with auto-filling @CreatedBy field (auto-filling does not work)
        // TODO remove this when auto-filling for @CreatedBy is fixed
        expense.setCreatedBy(auditorAware.getCurrentAuditor().get());

        Expense newExpense = expenseRepository.save(expense);
        return newExpense;
    }

  /**
   * Create expense in GBP
   * @param expenseWithCurrencyCreateDto
   * @return created expense from DB
   */
    public Mono<Expense> createExpenseWithoutRate(ExpenseWithCurrencyCreateDto expenseWithCurrencyCreateDto) {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<ExpenseWithCurrencyCreateDto>> errors = validator.validate(expenseWithCurrencyCreateDto);
      if (errors.size() > 0) {
        throw new ConstraintViolationException(errors);
      }

      // Check if amount has currency sign
      if (!hasCurrency(expenseWithCurrencyCreateDto.getAmount())) {
        // Simple creation without rate
        return Mono.just(createExpenseWithoutRate(ExpenseCreateProjectionDto.builder()
          .date(expenseWithCurrencyCreateDto.getDate())
          .reason(expenseWithCurrencyCreateDto.getReason())
          .amount(Double.parseDouble(expenseWithCurrencyCreateDto.getAmount().trim()))
          .build()));
      } else {
        return createExpenseWithRate(expenseWithCurrencyCreateDto);
      }
    }

    /**
     * DTO for service api.exchangeratesapi.io for getting rate of EUR->GBP by date
     */
    @Data
    public static class CurrencyRateDto {
      Map<String, Double> rates;
      String base;
    }

  /**
   * Converts amount (EUR -> GBP) using rate for the date inside DTO.
   * @param expenseWithCurrencyCreateDto dto with all params of expense
   * @return created Expense in DB
   */
    public Mono<Expense> createExpenseWithRate(ExpenseWithCurrencyCreateDto expenseWithCurrencyCreateDto) {
        String currentUserId = auditorAware.getCurrentAuditor().get();

        String date = expenseWithCurrencyCreateDto.getDate().format(DateTimeFormatter.ISO_DATE);
        Mono<CurrencyRateDto> mono = WebClient.create().get().uri("https://api.exchangeratesapi.io/" + date).retrieve().bodyToMono(CurrencyRateDto.class);
        Function<? super CurrencyRateDto, ? extends Expense> f = (rates) -> {
            Expense expense = Expense.builder()
                .date(expenseWithCurrencyCreateDto.getDate())
                .reason(expenseWithCurrencyCreateDto.getReason())
                .build();
            Double rate = rates.rates.get("GBP");
            expense.setAmount(rate * parseAmountWithCurrency(expenseWithCurrencyCreateDto.getAmount()));

            // Workaround for a problem with auto-filling @CreatedBy field (auto-filling does not work)
            // TODO remove this when auto-filling for @CreatedBy is fixed
            expense.setCreatedBy(currentUserId);

            Expense newExpense = expenseRepository.save(expense);
            return newExpense;
        };

        return mono.map(f);
    }

  /**
   * Parses string amount + currency (e.g. `10`, `10 EUR`) and extracts amount only.
   * @param amountWithCurrency
   * @return amount
   */
    public double parseAmountWithCurrency(String amountWithCurrency) {
        Matcher matcher = ExpenseWithCurrencyCreateDto.AMOUNT_WITH_CURRENCY_PATTERN.matcher(amountWithCurrency);
        matcher.matches();
        String amountStr = matcher.group(ExpenseWithCurrencyCreateDto.AMOUNT_GROUP_NUMBER);
        return Double.parseDouble(amountStr);
    }

  /**
   * Checks if string has currency postfix
   * @param amountWithCurrency
   * @return true if string has currency at the end of the string
   */
    public boolean hasCurrency(String amountWithCurrency) {
        Matcher matcher = ExpenseWithCurrencyCreateDto.AMOUNT_WITH_CURRENCY_PATTERN.matcher(amountWithCurrency);
        matcher.matches();
        String currencyStr = matcher.group(ExpenseWithCurrencyCreateDto.CURRENCY_GROUP_NUMBER);
        return currencyStr != null && !currencyStr.trim().isEmpty();
    }

}
