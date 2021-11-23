package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.exception.BudgetException;
import com.palette.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExpenseServiceTest {
    @Autowired ExpenseService expenseService;
    @Autowired BudgetService budgetService;
    @Autowired GroupRepository groupRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberGroupRepository memberGroupRepository;
    @Autowired BudgetRepository budgetRepository;
    @Autowired ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp(){
        Member member = new Member("wltn", "1234", "wogns","123");
        memberRepository.save(member);

        Group group = Group.builder().
                groupName("그룹1").
                groupsIntroduction("테스트 그룹 1입니다.")
                .build();
        groupRepository.save(group);

        MemberGroup memberGroup = new MemberGroup();
        memberGroupRepository.save(memberGroup);
        memberGroup.addMemberGroup(group,member);

        Group findGroup = groupRepository.findAll().get(0);

        Budget budget = new Budget(findGroup,1000000l);

        budgetService.addBudget(member,group,budget);
    }

    @BeforeEach
    void 지출_추가(){
        Member findMember = memberRepository.findAll().get(0);
        Group group = groupRepository.findAll().get(0);
        Budget budget = budgetRepository.findBudgetJoinWithGroup();

        Expense.Category category = Expense.Category.valueOf("TRANSPORTATION");
        Expense expense1 = Expense.builder()
                .category(category)
                .detail("마포에서 월곡 택시")
                .price(20000l)
                .build();

        Expense expense2 = Expense.builder()
                .category(category)
                .detail("공릉에서 노원역 택시")
                .price(10000l)
                .build();

        expenseService.addExpense(findMember,group,expense1,budget);
        expenseService.addExpense(findMember,group,expense2,budget);

        System.out.println("-----------------------before each-----------------------");
    }

    @Test
    void 지출_수정(){
        Group findGroup = groupRepository.findAll().get(0);
        Budget budget = findGroup.getBudget();
        Expense findExpense= budget.getExpenses().get(0);

        assertThat(findExpense.getDetail()).isEqualTo("마포에서 월곡 택시");

        Expense.Category category = Expense.Category.valueOf("TRANSPORTATION");
        Expense updateExpense = Expense.builder()
                .category(category)
                .detail("인천->부평 택시")
                .price(15000l)
                .build();
        ExpenseDto expenseDto = new ExpenseDto(updateExpense);
        expenseService.updateExpense(findExpense.getId(),expenseDto);
        assertThat(findExpense.getDetail()).isEqualTo("인천->부평 택시");
    }

    @Test
    void 지출_단건_삭제(){
        Group findGroup = groupRepository.findAll().get(0);
        Expense expense = findGroup.getBudget().getExpenses().get(0);
        expenseService.deleteExpense(findGroup.getBudget(),expense);
        assertThat(findGroup.getBudget().getExpenses().size()).isEqualTo(1);
    }

    @Test
    void 지출_전체_삭제(){
        Group findGroup = groupRepository.findAll().get(0);
        assertThat(findGroup.getBudget().getExpenses().size()).isEqualTo(2);
        expenseService.deleteAll(findGroup.getBudget());
        assertThat(findGroup.getBudget().getExpenses().size()).isEqualTo(0);
    }

    //존재하지 않는 예산일 때 exception 확인하기
    @Test
    void 예산_존재확인(){
        Member member = memberRepository.findAll().get(0);
        Group group = groupRepository.findAll().get(0);
        Budget budget = budgetRepository.findBudgetJoinWithExpenses();

        Expense.Category category = Expense.Category.valueOf("TRANSPORTATION");
        Expense expense = Expense.builder()
                .category(category)
                .detail("버스")
                .price(20000l)
                .build();

        budgetRepository.deleteById(budget.getId());

        Budget deletedBudget = budgetRepository.findBudgetJoinWithGroup();
        assertThatThrownBy(()->{
            expenseService.addExpense(member,group,expense,deletedBudget);
        }).isInstanceOf(BudgetException.class);
    }

}
