package com.engagetech.codechallenge.controllers;

import com.engagetech.codechallenge.data.dto.ExpenseCreateProjectionDto;
import com.engagetech.codechallenge.data.model.Expense;
import com.engagetech.codechallenge.data.projection.ExpenseIdLessProjection;
import com.engagetech.codechallenge.services.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST API (CRUD) Controller for `expense` resource.
 */
@RestController
@RequestMapping("/api/expenses")
@Slf4j
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @Operation(summary = "Get all expenses for current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all expenses for current user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseIdLessProjection.class))})
    })
    public List<ExpenseIdLessProjection> getExpenses(Principal principal) {
        List<ExpenseIdLessProjection> res = expenseService.getExpenses(ExpenseIdLessProjection.class);
        return res;
    }

    @PostMapping
    @Operation(summary = "Create expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created expense",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Expense.class)) }),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content)
    })
    public Expense createExpense(@RequestBody ExpenseCreateProjectionDto expenseCreateProjection) {
        return expenseService.createExpense(expenseCreateProjection);
    }
}
