package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.exception.GroupException;
import com.palette.repository.BudgetRepository;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BudgetService {

    private final GroupRepository groupRepository;
    private final BudgetRepository budgetRepository;
    private final MemberGroupRepository memberGroupRepository;

    //그룹에 예산 넣기
    @Transactional
    public Budget addBudget(Member member,Group group, Budget budget){
        isGroupExist(group);
        isMemberHaveAuthToUpdate(member,group);
        Budget saveBudget = new Budget(group,budget.getTotalBudget()); // todo: 이거 없이 바로 budget save해도 되지않낭
        budgetRepository.save(saveBudget); // todo: 수정해도 으차피 update되려낭..
        return saveBudget;
    }

    //그룹의 예산,경비,남은금액 조회
    @Transactional
    public BudgetResponseDto readBudget(Member member, Long id){
        Group group = groupRepository.findById(id).orElse(null);
        isGroupExist(group);
        isMemberHaveAuthToUpdate(member,group);
        Budget findBudget = budgetRepository.findBudgetJoinWithGroup();

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
        Budget findBudget = budgetRepository.findBudgetJoinWithGroup();
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
}
