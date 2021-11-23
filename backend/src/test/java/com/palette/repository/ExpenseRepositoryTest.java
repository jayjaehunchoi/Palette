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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ExpenseRepositoryTest {
    //지출 crud
    @Autowired GroupRepository groupRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired BudgetRepository budgetRepository;
    @Autowired ExpenseRepository expenseRepository;
    @Autowired EntityManager em;

    //expense create
    @BeforeEach
    void setUp(){
        //멤버,그룹만들고, 그 그룹의 budget만들고, 지출내역 만들기
        Group group = new Group("wltn group", "wltn's group");
        groupRepository.save(group);

        Group findGroup = groupRepository.findAll().get(0);

        Budget budget = new Budget(findGroup,1000000l);
        budgetRepository.save(budget);

        Expense.Category category = Expense.Category.TRANSPORTATION;
        Expense expense = new Expense(category,"574 버스",1200l);
        expense.saveExpenseOnBudget(budget);

        Expense.Category category2 = Expense.Category.FOOD;
        Expense expense2 = new Expense(category,"텐동키츠네",12000l);
        expense2.saveExpenseOnBudget(budget);
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
        Budget budget = budgetRepository.findAll().get(0);
        budget.getExpenses().remove(expense1);

        assertThat(expenseRepository.count()).isEqualTo(1);
    }

    @AfterEach
    void 전체_삭제(){
        System.out.println("-------------AfterEach-------------");
        memberRepository.deleteAll();
        groupRepository.deleteAll();
    }
}
