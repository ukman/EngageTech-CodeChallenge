package com.engagetech.codechallenge.data.model;

import com.engagetech.codechallenge.config.SecurityAuditorAware;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
public class Expense {

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
}
