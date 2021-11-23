package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class BudgetUpdateDto {
    @NotNull(message = "예산을 입력해주세요")
    private long totalBudget;

    public BudgetUpdateDto(long totalBudget) {
        this.totalBudget = totalBudget;
    }
}
