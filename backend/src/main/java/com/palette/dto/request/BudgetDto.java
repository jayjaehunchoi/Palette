package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class BudgetDto {
    @NotNull(message = "예산을 입력해주세요")
    @Max(value = 2000000000, message = "입력할 수 있는 최대 예산값을 초과하였습니다.")
    private long totalBudget;

    public BudgetDto(long totalBudget) {
        this.totalBudget = totalBudget;
    }
}
