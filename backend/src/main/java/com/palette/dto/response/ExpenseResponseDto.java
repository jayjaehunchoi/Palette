package com.palette.dto.response;

import com.palette.domain.group.Expense;
import com.palette.utils.annotation.EnumNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class ExpenseResponseDto {

    private Long expenseId;
    private Expense.Category category;
    private String detail;
    private long price;

    public ExpenseResponseDto(final Expense expense){
        this.expenseId = expense.getId();
        this.category = expense.getCategory();
        this.detail = expense.getDetail();
        this.price = expense.getPrice();
    }
}
