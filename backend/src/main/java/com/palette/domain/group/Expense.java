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
    private Long id;

    //종류 : 어떤 종류의 지출인지(교통,식비,숙박,기타)
    private String category;

    //자세한 내용 : 지출의 자세한 내용
    private String detail;

    //가격
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @Builder
    public Expense(String category, String detail, Long price, Budget budget){
        this.category = category;
        this.detail = detail;
        this.price = price;
        this.budget = budget;
    }

}
