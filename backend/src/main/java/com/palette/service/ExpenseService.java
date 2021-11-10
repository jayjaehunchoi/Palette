package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.ExpenseResponseDto;
import com.palette.repository.BudgetRepository;
import com.palette.repository.ExpenseRepository;
import com.palette.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final BudgetService budgetService;

    //todo: 지출변경

    @Transactional
    public Expense addExpense(Expense expense){
        Expense savedExpense = expenseRepository.save(expense);
        return savedExpense;
    }

    @Transactional
    public ExpenseResponseDto readExpenses(Long id){
        //todo: group 이랑 budget 있는지 확인 exception
        Group group = groupRepository.findById(id).orElse(null);
        Budget findBudget = budgetRepository.findByGroup(group).orElse(null);

        long totalBudget = findBudget.getTotalBudget();
        long totalExpense = 0l;
        long remainingBudget = totalBudget;

        for(int i = 0; i < findBudget.getExpenses().size(); i++){
            totalExpense += findBudget.getExpenses().get(i).getPrice();
        }

        remainingBudget = totalBudget - totalExpense;

        //그룹에 해당되는 expense 들만 list에 담아서 보내주기
        List<Expense> findExpenses = expenseRepository.findByBudget(findBudget);

        List<ExpenseDto> expenseDtoList = makeExpenseDtoList(findExpenses);

        ExpenseResponseDto dto = new ExpenseResponseDto(id,totalBudget,totalExpense,remainingBudget,expenseDtoList);
        return dto;
    }

    //expense List-> dto List로 바꿔주기
    public List<ExpenseDto> makeExpenseDtoList(List<Expense> expenses){

        return expenses.stream().map(ExpenseDto::new).collect(Collectors.toList());

/*      테스트완료하면 지우기
        List<ExpenseDto> expenseDtos = new ArrayList<>();
        for(int i = 0; i < expenses.size(); i++){
            String category = expenses.get(i).getCategory().name();
            String detail = expenses.get(i).getDetail();
            long price = expenses.get(i).getPrice();
            expenseDtos.add(new ExpenseDto(category,detail,price));
        }
        return expenseDtos;
 */
    }
}

