package com.palette.dto.response;

import com.palette.dto.request.ExpenseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ExpenseResponseDto {
    private Long groupId;
    private long totalBudget;
    private long totalExpense;
    private long remainingBudget;
    private List<ExpenseDto> expenses;

    public ExpenseResponseDto(Long groupId, long totalBudget, long totalExpense, long remainingBudget, List<ExpenseDto> expenses) {
        this.groupId = groupId;
        this.totalBudget = totalBudget;
        this.totalExpense = totalExpense;
        this.remainingBudget = remainingBudget;
        this.expenses = expenses;
    }
}
