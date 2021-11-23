package com.palette.controller;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.service.BudgetService;
import com.palette.service.ExpenseService;
import com.palette.service.GroupService;
import com.palette.controller.auth.AuthenticationPrincipal;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/travelgroup")
@RestController
public class ExpenseController {

    private final GroupService groupService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    //지출 추가
    @LoginChecker
    @PostMapping("/{travelgroupid}/expenses") //테스트완료, id 포함하지말기
    public Long addExpense(@AuthenticationPrincipal Member member, @RequestBody @Validated ExpenseDto expenseDto, @PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        Budget budget = budgetService.findByGroup(group);
        log.info("expense Category= {}", expenseDto.getCategory());
        Expense expense = expenseDto.toEntity();
        Expense savedExpense = expenseService.addExpense(member,group,expense,budget);
        return savedExpense.getId();
    }

    //지출 전체 삭제
    @LoginChecker
    @DeleteMapping("/{travelgroupid}/expenses") //테스트완료
    public ResponseEntity<Void> deleteExpenses(@PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        expenseService.deleteAll(group.getBudget());
        return HttpResponseUtil.RESPONSE_OK;
    }

    //지출 단건 수정
    @LoginChecker
    @PutMapping("/{travelgroupid}/expenses/{expenseid}") //테스트완료, id 포함해서 줘야함!!
    public ResponseEntity<Void> updateExpense(@RequestBody @Valid ExpenseDto expenseDto, @PathVariable("travelgroupid") Long travelGroupId, @PathVariable("expenseid") Long expenseId){
        expenseService.updateExpense(expenseId,expenseDto);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //지출 단건 삭제
    @LoginChecker
    @DeleteMapping("/{travelgroupid}/expenses/{expenseid}") //테스트완료 , id 포함해서 줘야함!!
    public ResponseEntity<Void> deleteExpense(@PathVariable("travelgroupid") Long travelGroupId, @PathVariable("expenseid") Long expenseId){
        Expense expense = expenseService.findById(expenseId);
        Budget budget = expense.getBudget();
        expenseService.deleteExpense(budget,expense);
        return HttpResponseUtil.RESPONSE_OK;
    }
}
