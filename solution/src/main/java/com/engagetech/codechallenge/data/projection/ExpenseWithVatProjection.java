package com.engagetech.codechallenge.data.projection;

public interface ExpenseWithVatProjection extends ExpenseIdLessProjection {
    double getVat();
}
