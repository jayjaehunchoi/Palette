package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.dto.request.BudgetDto;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.dto.response.ExpenseResponseDto;
import com.palette.repository.BudgetRepository;
import com.palette.repository.GroupRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class BudgetService {

    private final GroupRepository groupRepository;
    private final BudgetRepository budgetRepository;

    //그룹에 예산 넣기
    @Transactional
    public Budget addBudget(Group group, Budget budget){
        Budget saveBudget = new Budget(group,budget.getTotalBudget());
        budgetRepository.save(saveBudget);
        return saveBudget;
    }

    //그룹의 예산,경비,남은금액 조회
    @Transactional
    public BudgetResponseDto readBudget(Long id){
        //todo: group 이랑 budget 있는지 확인 exception
        Group group = groupRepository.findById(id).orElse(null);
        Budget findBudget = budgetRepository.findByGroup(group).orElse(null);

        long totalBudget = findBudget.getTotalBudget();
        long totalExpense = 0l;
        long remainingBudget = totalBudget;

        for(int i = 0; i < findBudget.getExpenses().size(); i++){
            //todo: 매번 반복문 돌리지말고 하나만 추가하는 방법 생각해보기, 그럼 db에 저장해야함
            totalExpense += findBudget.getExpenses().get(i).getPrice();
        }

        remainingBudget = totalBudget - totalExpense;

        BudgetResponseDto dto = new BudgetResponseDto(group.getId(),totalBudget,totalExpense,remainingBudget);
        return dto;
    }

    //budget의 그룹 조회
    public Budget findByGroup(Group group){
        Budget findBudget = budgetRepository.findByGroup(group).orElse(null);
        return findBudget;
    }

    //budget update
    @Transactional
    public Budget updateBudget(Long id, BudgetUpdateDto dto){
        Budget findBudget = budgetRepository.findById(id).orElse(null);
        findBudget.update(dto);
        return findBudget;
    }

    //budget delete
    @Transactional
    public void deleteBudget(Long id){
        Optional<Budget> budget = budgetRepository.findById(id);
        budget.ifPresent(selectGroup ->{
            budgetRepository.delete(selectGroup);
        });
    }
}
