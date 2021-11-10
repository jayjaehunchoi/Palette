package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class BudgetUpdateDto {
    @NotBlank(message = "예산을 입력해주세요")
    private long budget;
}
