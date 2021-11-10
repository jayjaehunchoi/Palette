package com.palette.dto.request;

import com.palette.domain.group.Expense;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ExpenseDto {

    @NotBlank(message = "지출 분류를 입력해주세요.")
    private String category;
    //todo: enum으로 바꿔보자아아아아ㅏ

    @NotBlank(message = "항목을 입력해주세요.")
    private String detail;

    @NotBlank(message = "금액을 입력해주세요.")
    @Max(value = 2000000000, message = "입력할 수 있는 최대 지출값을 초과하였습니다.")
    private long price;

    public ExpenseDto(String category, String detail, long price) {
        this.category = category;
        this.detail = detail;
        this.price = price;
    }

    public ExpenseDto(final Expense expense){
        this.category = expense.getCategory().name(); //todo: 이것도바꿔
        this.detail = expense.getDetail();
        this.price = expense.getPrice();
    }
}
