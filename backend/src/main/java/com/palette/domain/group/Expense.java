package com.palette.domain.group;

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
        //this.budget = budget;
    }

    public void saveExpenseOnBudget(Budget budget){
        this.budget = budget;
        budget.getExpenses().add(this);
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
    }
}
