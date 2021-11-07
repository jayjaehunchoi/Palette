package com.palette.dto.response;

import lombok.Getter;

@Getter
public class BudgetResponseDto {
    private Long groupId;
    private long totalBudget;

    public BudgetResponseDto(Long groupId, long totalBudget){
        this.groupId = groupId;
        this.totalBudget = totalBudget;
    }
}
