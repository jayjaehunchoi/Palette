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
import com.palette.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class BudgetServiceTest {
    @Autowired BudgetService budgetService;
    @Autowired GroupRepository groupRepository;
    @Autowired BudgetRepository budgetRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberGroupRepository memberGroupRepository;

    //그룹생성, 예산설정
    @BeforeEach
    void setup(){
        // 회원생성
        Member member = new Member("wltn", "1234", "wogns","123");
        memberRepository.save(member);

        Group group = Group.builder().
                groupName("그룹1").
                groupsIntroduction("테스트 그룹 1입니다.")
                .build();
        groupRepository.save(group);
        MemberGroup memberGroup = new MemberGroup();
        memberGroupRepository.save(memberGroup);
        memberGroup.addMemberGroup(group,member);

        Group findGroup = groupRepository.findAll().get(0);

        Budget budget = new Budget(findGroup,1000000l);

        budgetService.addBudget(member,group,budget);
    }

    @Test
    void 그룹의_예산정보_조회(){
        Group findGroup = groupRepository.findAll().get(0);
        Budget findBudget = budgetRepository.findBudgetJoinWithGroup();

        BudgetResponseDto budgetResponseDto = budgetService.readBudget(memberRepository.findAll().get(0),findGroup.getId());

        assertThat(budgetResponseDto.getGroupId()).isEqualTo(findBudget.getGroup().getId());
        assertThat(budgetResponseDto.getTotalBudget()).isEqualTo(findBudget.getTotalBudget());

        System.out.println(budgetResponseDto.getTotalExpense());
        System.out.println(budgetResponseDto.getRemainingBudget());
        System.out.println(budgetResponseDto.getExpenses().size());
    }

    //budget update 테스트
    @Test
    void 예산_업데이트(){
        Budget findBudget = budgetRepository.findAll().get(0);
        assertThat(findBudget.getTotalBudget()).isEqualTo(1000000l);

        BudgetUpdateDto budgetUpdateDto = new BudgetUpdateDto(200000l);
        budgetService.updateBudget(findBudget.getId(),budgetUpdateDto);

        assertThat(findBudget.getTotalBudget()).isEqualTo(200000l);
    }

    @Test
    void 예산_삭제(){
        Group group = groupRepository.findAll().get(0);
        Budget findBudget = budgetRepository.findBudgetJoinWithGroup();
        budgetService.deleteBudget(group);
        assertThat(budgetRepository.findBudgetJoinWithGroup()).isEqualTo(null);
    }
    @Test
    void 접근권환_확인(){
        Member notGrantMember = new Member("easy", "1234", "easy","123124");
        memberRepository.save(notGrantMember);

        Group findGroup = groupRepository.findAll().get(0);
        Budget budget = new Budget(findGroup,50000l);

        assertThatThrownBy(() -> {
            budgetService.addBudget(notGrantMember,findGroup,budget);
        }).isInstanceOf(GroupException.class).hasMessage("그룹 접근 권한이 없습니다.");
    }
}
