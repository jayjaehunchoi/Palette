package com.palette.domain.group;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column
    private Long preparedBudget;

    //지출 기록
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @Builder
    public Budget(Group group, Long preparedBudget, List<Expense> expenses){
        this.group = group;
        this.preparedBudget = preparedBudget;
        this.expenses = expenses;
    }
}
