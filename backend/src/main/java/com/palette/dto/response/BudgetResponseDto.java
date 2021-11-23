package com.palette.dto.response;

import com.palette.dto.request.ExpenseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BudgetResponseDto {
    private Long groupId;
    private long totalBudget;
    private long totalExpense;
    private long remainingBudget;
    private List<ExpenseResponseDto> expenses;

    public BudgetResponseDto(Long groupId, long totalBudget, long totalExpense, long remainingBudget, List<ExpenseResponseDto> expenses) {
        this.groupId = groupId;
        this.totalBudget = totalBudget;
        this.totalExpense = totalExpense;
        this.remainingBudget = remainingBudget;
        this.expenses = expenses;
    }
}
