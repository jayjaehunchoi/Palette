package com.palette.domain.group;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_budget_group"))
    private Group group;

    private long totalBudget;

    //지출 기록
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @Builder
    public Budget(Group group, Long totalBudget){
        this.group = group;
        this.totalBudget = totalBudget;
    }

    public void saveBudgetOnGroup(Group group){
        this.group = group;
        group.setBudget(this);
    }

    public void deleteAllExpenses(){
        this.expenses.clear();
    }

    public void update(long totalBudget){
        this.totalBudget = totalBudget;
        this.group.setBudget(this);
    }
}
