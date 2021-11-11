package com.palette.controller;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.ExpenseResponseDto;
import com.palette.service.BudgetService;
import com.palette.service.ExpenseService;
import com.palette.service.GroupService;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/travelgroup")
@RestController
public class ExpenseController {

    private final GroupService groupService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    @GetMapping("/{travelgroupid}/expense")
    public ExpenseResponseDto readExpense(@Login Member member, @PathVariable("travelgroupid") long travelGroupId){
        return expenseService.readExpenses(member,travelGroupId);
    }

    //지출 추가
    @PostMapping("/{travelgroupid}/expense")
    public Long addExpense(@Login Member member, @RequestBody @Validated ExpenseDto expenseDto, @PathVariable("travelgroupid") long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        Budget budget = budgetService.findByGroup(group);

        Expense.Category category = Expense.Category.valueOf(expenseDto.getCategory());
        Expense expense = Expense.builder()
                .category(category)
                .detail(expenseDto.getDetail())
                .price(expenseDto.getPrice())
                .build();
        Expense savedExpense = expenseService.addExpense(member,group,expense,budget);
        return savedExpense.getId();
    }
}
