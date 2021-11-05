package com.palette.repository;

import com.palette.domain.group.Group;
import com.palette.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class GroupRepositoryTest {
    //group repository crud test
    @Autowired private MemberRepository memberRepository;
    @Autowired private GroupRepository groupRepository;

    @BeforeEach
    void setUp(){ //그룹 create test
        Group group = new Group("wltn group", "wltn's group");
        groupRepository.save(group);

        Group group2 = new Group("1ho group","1ho's group");
        groupRepository.save(group2);
    }
    @Test
    void 그룹_조회(){ //read
        for(int i = 0; i < 2; i++) {
            groupRepository.findAll().get(i);
        }
    }

    @Test
    void 그룹_삭제(){
        Group findGroup = groupRepository.findById(1L).orElse(null);
        if(findGroup != null){
            groupRepository.delete(findGroup);
        }
        assertThat(groupRepository.findAll().size()).isEqualTo(1);
    }
}
