package com.palette.dto.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
public class BudgetDto {
    @NotBlank(message = "예산을 입력해주세요")
    @Max(2000000000)
    private long totalBudget;
}
