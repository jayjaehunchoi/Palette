package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.dto.response.ExpenseResponseDto;
import com.palette.exception.BudgetException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BudgetService {

    private final GroupRepository groupRepository;
    private final BudgetRepository budgetRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final ExpenseRepository expenseRepository;

    //그룹에 예산 넣기
    @Transactional
    public Long addBudget(Member member,Group group, Budget budget){
        isGroupExist(group);
        isMemberHaveAuthToUpdate(member,group);
        isBudgetAlreadyExist(group);
        Budget saveBudget = new Budget(group,budget.getTotalBudget());
        budgetRepository.save(saveBudget);
        saveBudget.saveBudgetOnGroup(group);
        return saveBudget.getId();
    }

    //그룹의 예산,경비,남은금액 조회
    @Transactional
    public BudgetResponseDto readBudget(Member member, Long id){
        Group group = groupRepository.findById(id).orElse(null);
        isGroupExist(group);
        isBudgetExist(group);
        isMemberHaveAuthToUpdate(member,group);
        Budget findBudget = budgetRepository.findBudgetByGroupId(id);

        long totalBudget = findBudget.getTotalBudget();
        long totalExpense = 0l;
        long remainingBudget = totalBudget;

        for(int i = 0; i < findBudget.getExpenses().size(); i++){
            totalExpense += findBudget.getExpenses().get(i).getPrice();
        }

        remainingBudget = totalBudget - totalExpense;

        //그룹에 해당되는 expense 들만 list에 담아서 보내주기
        List<Expense> findExpenses = expenseRepository.findByBudgetId(findBudget.getId());
        List<ExpenseResponseDto> expenseDtoList = makeExpenseDtoList(findExpenses);

        BudgetResponseDto dto = new BudgetResponseDto(id,totalBudget,totalExpense,remainingBudget,expenseDtoList);
        return dto;
    }

    //budget의 그룹 조회
    public Budget findByGroup(Group group){
        Budget findBudget = budgetRepository.findBudgetByGroupId(group.getId());
        return findBudget;
    }

    //budget update
    @Transactional
    public Budget updateBudget(Long id, BudgetUpdateDto dto){
        Budget findBudget = budgetRepository.findById(id).orElse(null);
        log.info("update budget = {}" ,dto.getTotalBudget());
        findBudget.update(dto.getTotalBudget());
        return findBudget;
    }

    //budget delete
    @Transactional
    public void deleteBudget(Group group){
        isBudgetExist(group);
        Budget budget = budgetRepository.findById(group.getBudget().getId()).orElse(null);
        budgetRepository.delete(budget);
        budget.getGroup().setBudget(null);

    }

    //그룹 수정 권한이 있는지 확인 (그룹의 멤버인지 확인)
    public void isMemberHaveAuthToUpdate(Member member,Group group){
        MemberGroup memberGroup = memberGroupRepository.findByMemberAndGroup(member,group);
        if(memberGroup == null){
            log.error("Group Access Grant Error");
            throw new GroupException("그룹 접근 권한이 없습니다.");
        }
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
        Budget findBudget = budgetRepository.findBudgetByGroupId(group.getId());
        if(findBudget == null){
            log.error("Budget Not Exist Error");
            throw new BudgetException("예산이 존재하지 않습니다.");
        }
    }

    //이미 예산이 존재하는지 확인
    private void isBudgetAlreadyExist(Group group){
        Budget findBudget = budgetRepository.findBudgetByGroupId(group.getId());
        if(findBudget != null){
            log.error("Budget Is Already Exists");
            throw new BudgetException("예산이 이미 존재합니다.");
        }
    }

    private List<ExpenseResponseDto> makeExpenseDtoList(List<Expense> expenses){
        return expenses.stream().map(ExpenseResponseDto::new).collect(Collectors.toList());
    }

}
