package com.palette.domain.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palette.dto.request.ExpenseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    //종류 : 어떤 종류의 지출인지(교통,식비,숙박,기타) EnumType으로 설정
    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    //자세한 내용 : 지출의 자세한 내용
    private String detail;

    //가격
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id",foreignKey = @ForeignKey(name = "fk_expense_budget"))
    private Budget budget;

    @Builder
    public Expense(Category category, String detail, Long price){
        this.category = category;
        this.detail = detail;
        this.price = price;
    }

    public void saveExpenseOnBudget(Budget budget){
        this.budget = budget;
        budget.getExpenses().add(this);
    }

    public void update(ExpenseDto expenseDto){
        this.category = expenseDto.getCategory();
        this.detail = expenseDto.getDetail();
        this.price = expenseDto.getPrice();
    }

    public void deleteExpense(Budget budget){
        budget.getExpenses().remove(this);
    }

    public enum Category{
        TRANSPORTATION("교통"),
        FOOD("식비"),
        LODGING("숙박"),
        ETC("기타");

        private String categoryName;

        Category(String categoryName) {
            this.categoryName = categoryName;
        }


        @JsonValue
        public String getCategoryName() {
            return categoryName;
        }

        //@JsonCreator
        public static Category getCategoryFromCategoryName(String categoryName){
            for (Category category : Category.values()) {
                if(category.categoryName.equals(categoryName) || category.name().equals(categoryName)){
                    return category;
                }
            }
            return null;
        }

    }
}
