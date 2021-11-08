package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.BudgetDto;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.request.GroupUpdateDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.repository.BudgetRepository;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import com.palette.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final BudgetRepository budgetRepository;

    //그룹 최초 추가
    @Transactional
    public void addGroup(Group group, Member member){
        groupRepository.save(group);
        MemberGroup memberGroup = new MemberGroup();
        memberGroupRepository.save(memberGroup);
        memberGroup.addMemberGroup(group,member);
    }

    //그룹에 멤버 추가
    @Transactional
    public void addGroupMember(Long id,Member member){
        Optional<Group> findGroup = groupRepository.findById(id);
        findGroup.ifPresent(selectGroup -> {
            MemberGroup memberGroup = new MemberGroup();
            memberGroupRepository.save(memberGroup);
            memberGroup.addMemberGroup(selectGroup,member);
        });
    }

    //그룹 멤버 삭제
    @Transactional
    public void deleteGroupMember(Long id,Member member){
        Optional<Group> findGroup = groupRepository.findById(id);
        findGroup.ifPresent(selectGroup ->{
            // todo: memberGroup 삭제, 그 멤버그룹이랑 이어져있는 member과 group의 membergroup 삭제
            MemberGroup findMemberGroup = memberGroupRepository.findByMemberGroup(member,selectGroup);
            findMemberGroup.deleteMemberGroup(selectGroup,member);
        });
    }

    //그룹 업데이트
    @Transactional
    public void updateGroup(Long id, GroupUpdateDto dto){
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(selectGroup ->{
            selectGroup.updateGroup(dto);
        });
    }

    //그룹삭제
    @Transactional
    public void deleteGroup(Long id){
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(selectGroup ->{
            groupRepository.deleteById(id);
        });
        // todo: 그룹없다면 exception 처리, 재훈이코드 보고 참고해야지
    }

    //그룹에 예산 넣기
    @Transactional
    public void addBudget(Long id, BudgetDto budgetDto){
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(selectGroup ->{ //todo: budgetDto null check
            Budget budget = new Budget(selectGroup,budgetDto.getTotalBudget());
            budgetRepository.save(budget);
        });
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
