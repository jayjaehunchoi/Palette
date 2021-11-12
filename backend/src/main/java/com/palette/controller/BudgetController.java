package com.palette.controller;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.GeneralResponse;
import com.palette.dto.request.BudgetDto;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.domain.group.Expense;
import com.palette.dto.response.ExpenseResponseDto;
import com.palette.service.BudgetService;
import com.palette.service.ExpenseService;
import com.palette.service.GroupService;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/travelgroup")
@RestController
public class BudgetController {

    private final GroupService groupService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    //todo: 예산/지출 수정(put),삭제(delete)
    @GetMapping("/{travelgroupid}/totalbudget")
    public BudgetResponseDto readBudget(@Login Member member,@PathVariable("travelgroupid") long travelGroupId){
        return budgetService.readBudget(member,travelGroupId);
    }


    //총 예산 추가
    @PostMapping("/{travelgroupid}/totalbudget") // Restful url : 동사대신 명사와 http 메소드로 표현
    public Long saveBudget(@Login Member member, @RequestBody @Validated BudgetDto budgetDto, @PathVariable("travelgroupid") long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        Budget budget = Budget.builder()
                        .group(group)
                        .totalBudget(budgetDto.getTotalBudget())
                        .build();
        Budget savedBudget = budgetService.addBudget(member,group,budget);
        return savedBudget.getId();
    }


}
