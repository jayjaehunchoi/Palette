package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.exception.BudgetException;
import com.palette.exception.ExpenseException;
import com.palette.exception.GroupException;
import com.palette.repository.BudgetRepository;
import com.palette.repository.ExpenseRepository;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;

    @Transactional
    public Expense findById(Long id){
        Expense expense = expenseRepository.findById(id).orElse(null);
        isExpenseExists(expense);
        return expense;
    }

    //지출 추가
    @Transactional
    public Expense addExpense(Member member,Group group,Expense expense,Budget budget){
        isGroupExist(group);
        isMemberHaveAuthToUpdate(member,group);
        isBudgetExist(group);
        expense.saveExpenseOnBudget(budget);
        return expense;
    }

    //지출 수정
    @Transactional
    public void updateExpense(Long id, ExpenseDto expenseDto){
        Expense expense = findById(id);
        expense.update(expenseDto);
    }

    //지출 전체 삭제
    public void deleteAll(Budget budget){
        budget.deleteAllExpenses();
    }

    //지출 단건 삭제
    public void deleteExpense(Budget budget, Expense expense){
        expense.deleteExpense(budget);
    }


    //expense List-> dto List로 바꿔주기
    public List<ExpenseDto> makeExpenseDtoList(List<Expense> expenses){
        return expenses.stream().map(ExpenseDto::new).collect(Collectors.toList());
    }

    //그룹이 존재하는지 확인
    private void isGroupExist(Group findGroup) {
        if (findGroup == null) {
            log.error("Group Not Exist Error");
            throw new GroupException("존재하지 않는 그룹입니다.");
        }
    }

    //그룹에 Budget이 존재하는지 확인
    private void isBudgetExist(Group group){
        Budget findBudget = budgetRepository.findBudgetJoinWithGroup();
        if(findBudget == null){
            log.error("Budget Not Exist Error");
            throw new BudgetException("예산이 존재하지 않습니다.");
        }
    }

    //지출 수정 권한이 있는지 확인 (그룹의 멤버인지 확인)
    public void isMemberHaveAuthToUpdate(Member member,Group group){
        MemberGroup memberGroup = memberGroupRepository.findByMemberAndGroup(member,group);
        if(memberGroup == null){
            log.error("Group Access Grant Error");
            throw new GroupException("그룹 접근 권한이 없습니다.");
        }
    }

    public void isExpenseExists(Expense expense){
        if(expense == null){
            log.error("Expense Not Exists Error");
            throw new ExpenseException("존재 하지 않는 지출입니다.");
        }
    }
}

