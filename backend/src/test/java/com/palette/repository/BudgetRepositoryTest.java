package com.palette.repository;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class BudgetRepositoryTest {
    //budget crud test
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberGroupRepository memberGroupRepository;
    @Autowired
    BudgetRepository budgetRepository;

    @BeforeEach
    void setUp(){ //예산 create test
        Group group = new Group("wltn group", "wltn's group");
        groupRepository.save(group);

        Group findGroup = groupRepository.findAll().get(0);

        //budget은 한 그룹당 하나만 만들어져야한다. 업데이트는 업데이트 메소드를 이용할것
        Budget budget = new Budget(findGroup,1000000l);
        budgetRepository.save(budget);
    }

    @Test
    void 예산_조회(){ //read
        Group findGroup = groupRepository.findAll().get(0);
        Budget findBudget = budgetRepository.findAll().get(0);
        assertThat(findGroup).isEqualTo(findBudget.getGroup());
    }

    @Test
    void 예산_삭제(){ //delete
        Budget findBudget = budgetRepository.findAll().get(0);
        if(findBudget != null){
            budgetRepository.delete(findBudget);
        }
        assertThat(budgetRepository.findAll().size()).isEqualTo(0);
    }
}
