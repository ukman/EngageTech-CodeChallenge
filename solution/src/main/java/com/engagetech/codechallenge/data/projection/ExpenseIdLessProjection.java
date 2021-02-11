package com.engagetech.codechallenge.data.projection;

import java.time.LocalDate;

/**
 * Projection for expense (minimum info for usual user)
 */
public interface ExpenseIdLessProjection {
    double getAmount();
    String getReason();
    LocalDate getDate();
}
