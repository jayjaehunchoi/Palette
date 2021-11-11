package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.repository.BudgetRepository;
import com.palette.repository.GroupRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class BudgetServiceTest {
    @Autowired BudgetService budgetService;
    @Autowired GroupRepository groupRepository;
    @Autowired BudgetRepository budgetRepository;

    //그룹생성, 예산설정
    @BeforeEach
    void setup(){
        Group group = Group.builder().
                groupName("그룹1").
                groupsIntroduction("테스트 그룹 1입니다.")
                .build();
        groupRepository.save(group);

        Group findGroup = groupRepository.findAll().get(0);

        Budget budget = new Budget(findGroup,1000000l);

        budgetService.addBudget(group,budget);
    }

    @Test
    void 그룹의_예산정보_조회(){
        Group findGroup = groupRepository.findAll().get(0);
        Budget findBudget = budgetRepository.findByGroup(findGroup).orElse(null);

        BudgetResponseDto budgetResponseDto = budgetService.readBudget(findGroup.getId());

        assertThat(budgetResponseDto.getGroupId()).isEqualTo(findBudget.getGroup().getId());
        assertThat(budgetResponseDto.getTotalBudget()).isEqualTo(findBudget.getTotalBudget());

        System.out.println(budgetResponseDto.getTotalExpense());
        System.out.println(budgetResponseDto.getRemainingBudget());
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


}
