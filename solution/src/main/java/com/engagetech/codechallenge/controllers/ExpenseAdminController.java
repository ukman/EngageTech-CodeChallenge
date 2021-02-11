package com.engagetech.codechallenge.controllers;

import com.engagetech.codechallenge.data.model.Expense;
import com.engagetech.codechallenge.services.ExpenseAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/expenses")
public class ExpenseAdminController {
    private final ExpenseAdminService expenseAdminService;

    public ExpenseAdminController(ExpenseAdminService expenseAdminService) {
        this.expenseAdminService = expenseAdminService;
    }

    @GetMapping
    @Operation(summary = "Get all expenses for all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all expenses for all users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Expense.class))})
    })
    public List<Expense> getExpenses(Principal principal) {
        List<Expense> res = expenseAdminService.getExpenses();
        return res;
    }


}
