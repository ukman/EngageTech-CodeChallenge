package com.engagetech.codechallenge.data.model;

import com.engagetech.codechallenge.config.SecurityAuditorAware;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Currency;

/**
 * Expense data model (entity) for ORM
 */
@Data
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@EntityListeners(SecurityAuditorAware.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
  // TODO Inject it from config file
  /**
   * Vat rate for calculation VAT amount for expenses
   */
  public static final double VAT_RATE = 0.2;

  @Id
    @GeneratedValue
    private Long id;

    /**
     * Date of expense
     */
    @PastOrPresent
    @Basic(optional = false)
    private LocalDate date;

    /**
     * Amount of expense (money)
     */
    @PositiveOrZero
    @Column(columnDefinition = "numeric(18,2)")
    @Basic(optional = false)
    private double amount;

    /**
     * Reason (goal) of expense
     */
    private String reason;

    /**
     * Assign expense to a user
     */
    @CreatedBy
    private String createdBy;

    @Formula("round(amount * " + VAT_RATE + ", 2)")
    private double vat;
}
