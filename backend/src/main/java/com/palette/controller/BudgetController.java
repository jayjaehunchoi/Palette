package com.palette.controller;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.request.BudgetDto;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.service.BudgetService;
import com.palette.service.ExpenseService;
import com.palette.service.GroupService;
import com.palette.utils.annotation.Login;
import com.palette.utils.constant.HttpResponseUtil;
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

    @GetMapping("/{travelgroupid}/budget")
    public BudgetResponseDto readBudget(@Login Member member, @PathVariable("travelgroupid") long travelGroupId){
        return budgetService.readBudget(member,travelGroupId);
    }

    //총 예산 추가
    @PostMapping("/{travelgroupid}/budget") // Restful url : 동사대신 명사와 http 메소드로 표현
    public ResponseEntity<Void> saveBudget(@Login Member member, @RequestBody @Validated BudgetDto budgetDto, @PathVariable("travelgroupid") long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        Budget budget = Budget.builder()
                        .group(group)
                        .totalBudget(budgetDto.getTotalBudget())
                        .build();
        budgetService.addBudget(member,group,budget);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //예산 수정
    @PutMapping("/{travelgroupid}/budget")
    public ResponseEntity<Void> updateBudget(@Login Member member, @RequestBody @Validated BudgetUpdateDto budgetUpdateDto, @PathVariable("travelgroupid") long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        Budget budget = Budget.builder()
                .group(group)
                .totalBudget(budgetUpdateDto.getBudget())
                .build();
        budgetService.updateBudget(group.getBudget().getId(),budgetUpdateDto);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //예산 삭제
    @DeleteMapping("/{travlegroupid}/budget")
    public ResponseEntity<Void> deleteBudget(@Login Member member,@PathVariable("travelgroupid") long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        budgetService.deleteBudget(group.getBudget().getId());
        return HttpResponseUtil.RESPONSE_OK;
    }
}
