package com.palette.repository;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {
    @Query("select b from Budget b join Group g on g.id = b.group.id")
    Budget findBudgetJoinWithGroup();

    @Query("select b from Budget b join Expense e on b.id = e.budget.id")
    Budget findBudgetJoinWithExpenses();
}
