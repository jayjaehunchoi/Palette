package com.palette.dto.request;

import com.palette.domain.group.Expense;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class ExpenseDto {

    private Long id;

    @NotNull(message = "지출 분류를 입력해주세요.")
    private Expense.Category category;

    @NotBlank(message = "항목을 입력해주세요.")
    private String detail;

    @NotNull(message = "금액을 입력해주세요.")
    @Max(value = 2000000000, message = "입력할 수 있는 최대 지출값을 초과하였습니다.")
    private long price;

    public ExpenseDto(final Expense expense){
        this.id = expense.getId();
        this.category = expense.getCategory();
        this.detail = expense.getDetail();
        this.price = expense.getPrice();
    }
}
