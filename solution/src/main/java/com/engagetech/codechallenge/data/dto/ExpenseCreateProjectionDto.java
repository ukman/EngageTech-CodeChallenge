package com.engagetech.codechallenge.data.dto;

import com.engagetech.codechallenge.data.projection.ExpenseIdLessProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for creating expenses.
 */
@Data
@Builder
public class ExpenseCreateProjectionDto implements ExpenseIdLessProjection {
    private double amount;
    private String reason;
    private LocalDate date;
}
