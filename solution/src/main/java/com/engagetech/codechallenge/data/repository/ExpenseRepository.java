package com.engagetech.codechallenge.data.repository;

import com.engagetech.codechallenge.data.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    <T> List<T> findBy(Class<T> projectionType);
    <T> List<T> findByCreatedBy(String createdBy, Class<T> projectionType);
}
