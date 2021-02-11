package com.engagetech.codechallenge.services;

import com.engagetech.codechallenge.data.model.Expense;
import com.engagetech.codechallenge.data.projection.ExpenseIdLessProjection;
import com.engagetech.codechallenge.data.repository.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Expense createExpense(ExpenseIdLessProjection expenseCreateProjection) {
        Expense expense = modelMapper.map(expenseCreateProjection, Expense.class);

        // Workaround for a problem with auto-filling @CreatedBy field (auto-filling does not work)
        // TODO remove this when auto-filling for @CreatedBy is fixed
        expense.setCreatedBy(auditorAware.getCurrentAuditor().get());

        Expense newExpense = expenseRepository.save(expense);
        return newExpense;
    }
}
