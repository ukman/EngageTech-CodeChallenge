package com.engagetech.codechallenge;

import com.engagetech.codechallenge.data.model.Expense;
import com.engagetech.codechallenge.data.projection.ExpenseIdLessProjection;
import com.engagetech.codechallenge.data.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class JpaTests {
  @Autowired
  private ExpenseRepository expenseRepository;

  private static double[][] EXP_VATS = new double[][]{
    {100, 20},
    {10, 2},
    {1, 0.2},
    {0.1, 0.02},
    {0.01, 0.},
    {1.23, 0.25},
    {1.22, 0.24},
    {2, 0.4},
  };

  /**
   * This test checks if VAT calculation works fine after getting expenses from DB
   */
  @Test
  public void testExpenseVat() {
    for (double[] expVat : EXP_VATS) {
      Expense expense = Expense.builder()
        .amount(expVat[0])
        .date(LocalDate.now())
        .reason("Bread")
        .build();
      Expense newExp = expenseRepository.save(expense);
      Expense fetchedExp = expenseRepository.findById(newExp.getId()).get();
      double vat = fetchedExp.getVat();
      assertEquals(expVat[1], vat);
    }
  }
}
