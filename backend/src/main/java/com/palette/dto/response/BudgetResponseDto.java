package com.palette.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BudgetResponseDto {
    private Long groupId;
    private long totalBudget;
    private long totalExpense;
    private long remainingBudget;

    public BudgetResponseDto(Long groupId, long totalBudget, long totalExpense, long remainingBudget){
        this.groupId = groupId;
        this.totalBudget = totalBudget;
        this.totalExpense = totalExpense;
        this.remainingBudget = remainingBudget;
    }
}
