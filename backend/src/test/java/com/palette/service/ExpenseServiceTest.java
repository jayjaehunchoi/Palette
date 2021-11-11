package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.ExpenseResponseDto;
import com.palette.repository.BudgetRepository;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import com.palette.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class ExpenseServiceTest {
    @Autowired ExpenseService expenseService;
    @Autowired BudgetService budgetService;
    @Autowired GroupRepository groupRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberGroupRepository memberGroupRepository;
    @Autowired BudgetRepository budgetRepository;

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
        Budget budget = budgetRepository.findByGroup(group).orElse(null);

        Expense.Category category = Expense.Category.valueOf("TRANSPORTATION");
        Expense expense1 = Expense.builder()
                .category(category)
                .detail("마포에서 월곡 택시")
                .price(20000l)
                .budget(budget)
                .build();

        Expense expense2 = Expense.builder()
                .category(category)
                .detail("공릉에서 노원역 택시")
                .price(10000l)
                .budget(budget)
                .build();

        expenseService.addExpense(findMember,group,expense1);
        expenseService.addExpense(findMember,group,expense2);
    }

    @Test
    void 지출_전체_조회(){
        Member member = memberRepository.findAll().get(0);
        Group findGroup = groupRepository.findAll().get(0);
        ExpenseResponseDto dto = expenseService.readExpenses(member,findGroup.getId());
        List<ExpenseDto> expenses = dto.getExpenses();

        assertThat(expenses.get(0).getDetail()).isEqualTo("마포에서 월곡 택시");
        assertThat(expenses.get(1).getDetail()).isEqualTo("공릉에서 노원역 택시");

    }

    //todo: 지출 단권 수정
    //todo: 지출 단권 삭제
    //todo: 지출 전체 삭제
}
