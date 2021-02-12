package com.engagetech.codechallenge.controllers;

import com.engagetech.codechallenge.data.model.Expense;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vats")
public class VatController {
    /**
     * Get vat for UK.
     * @return VAT for UK
     */
    // TODO redesign for different countries
    @GetMapping("/uk")
    double getUkVat() {
        return Expense.VAT_RATE;
    }
}
