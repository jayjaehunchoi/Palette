package com.palette.repository;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByBudgetId(Long budgetId);
}
