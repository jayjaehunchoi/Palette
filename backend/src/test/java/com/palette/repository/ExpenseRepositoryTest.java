package com.palette.repository;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ExpenseRepositoryTest {
    //지출 crud
    @Autowired GroupRepository groupRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired BudgetRepository budgetRepository;
    @Autowired ExpenseRepository expenseRepository;

    //expense create
    @BeforeEach
    void setUp(){
        //멤버,그룹만들고, 그 그룹의 budget만들고, 지출내역 만들기
        Group group = new Group("wltn group", "wltn's group");
        groupRepository.save(group);

        Group findGroup = groupRepository.findAll().get(0);

        Budget budget = new Budget(findGroup,1000000L);
        budgetRepository.save(budget);

        Expense.Category category = Expense.Category.TRANSPORTATION;
        Expense expense = new Expense(category,"574 버스",1200L, budget);
        expenseRepository.save(expense);

        Expense.Category category2 = Expense.Category.FOOD;
        Expense expense2 = new Expense(category,"텐동키츠네",12000L, budget);
        expenseRepository.save(expense2);

    }

    @Test
    void 지출_조회(){
        Budget budget = budgetRepository.findAll().get(0);

        Expense expense1 = expenseRepository.findAll().get(0);
        Expense expense2 = expenseRepository.findAll().get(1);

        assertThat(expense1.getBudget()).isEqualTo(budget);
        assertThat(expense2.getBudget()).isEqualTo(budget);

        assertThat(expense1.getDetail()).isEqualTo("574 버스");
        assertThat(expense2.getDetail()).isEqualTo("텐동키츠네");
    }

    @Test
    void 지출_삭제(){
        Expense expense1 = expenseRepository.findAll().get(0);
        expenseRepository.delete(expense1);

        assertThat(expenseRepository.count()).isEqualTo(1);
    }

    @AfterEach
    void 전체_삭제(){
        System.out.println("-------------AfterEach-------------");
        memberRepository.deleteAll();
        groupRepository.deleteAll();
    }
}
