package com.palette.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class BudgetUpdateDto {
    @NotBlank(message = "예산을 입력해주세요")
    private long budget;
}
