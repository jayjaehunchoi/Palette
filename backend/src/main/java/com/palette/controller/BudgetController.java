package com.palette.controller;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.request.BudgetDto;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.service.BudgetService;
import com.palette.service.GroupService;
import com.palette.controller.auth.AuthenticationPrincipal;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/travelgroup")
@RestController
public class BudgetController {

    private final GroupService groupService;
    private final BudgetService budgetService;

    //budget 정보 읽기
    @LoginChecker
    @GetMapping("/{travelgroupid}/budget") //테스트완료
    public BudgetResponseDto readBudget(@AuthenticationPrincipal Member member, @PathVariable("travelgroupid") Long travelGroupId){
        return budgetService.readBudget(member,travelGroupId);
    }

    //총 예산 추가
    @ResponseStatus(HttpStatus.CREATED)
    @LoginChecker
    @PostMapping("/{travelgroupid}/budget") //테스트완료 (중복추가안되게도 완료)
    public Long saveBudget(@AuthenticationPrincipal Member member, @RequestBody @Valid BudgetDto budgetDto, @PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        Budget budget = Budget.builder()
                        .group(group)
                        .totalBudget(budgetDto.getTotalBudget())
                        .build();
        return budgetService.addBudget(member,group,budget);
    }

    //예산 수정
    @LoginChecker
    @PutMapping("/{travelgroupid}/budget") //테스트완료
    public ResponseEntity<Void> updateBudget(@RequestBody @Valid BudgetUpdateDto budgetUpdateDto, @PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        log.info("dto budget = {}",budgetUpdateDto.getTotalBudget());
        budgetService.updateBudget(group.getBudget().getId(),budgetUpdateDto);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //예산 삭제
    @LoginChecker
    @DeleteMapping("/{travelgroupid}/budget") //테스트완료
    public ResponseEntity<Void> deleteBudget(@PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        budgetService.deleteBudget(group);
        return HttpResponseUtil.RESPONSE_OK;
    }
}
