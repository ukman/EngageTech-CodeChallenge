package com.engagetech.codechallenge.services;

import com.engagetech.codechallenge.data.model.Expense;
import com.engagetech.codechallenge.data.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Expense service for admin
 */
@Service
public class ExpenseAdminService {

    private final ExpenseRepository expenseRepository;

    private final AuditorAware<String> auditorAware;

    @Autowired
    public ExpenseAdminService(ExpenseRepository expenseRepository, AuditorAware<String> auditorAware) {
        this.expenseRepository = expenseRepository;
        this.auditorAware = auditorAware;
    }

    /**
     * Get all expenses for all users
     * @return list of all expenses
     */
    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

}
