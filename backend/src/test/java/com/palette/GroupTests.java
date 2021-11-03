package com.palette;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import com.palette.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class GroupTests {
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberGroupRepository memberGroupRepository;

    @BeforeEach
    public void setting() {

        // 회원생성
        Member member1 = new Member("1234", "wltn", "wltnfile");

        Member member2 = new Member("1234", "skfk", "skfkfile");

        Member member3 = new Member("1234", "wogns", "wognsfile");

        Member member4 = new Member("1234", "ruddls", "ruddlsfile");

        // 그룹생성
        Group group = Group.builder().
                groupName("그룹1").
                groupIntroduction("테스트 그룹 1입니다.").
                membersNumber(0L).build();

        Group group2 = Group.builder()
                .groupName("그룹2")
                .groupIntroduction("테스트 그룹 2입니다")
                .membersNumber(0L).build();


        // 멤버 그룹1 생성
        List<MemberGroup> memberGroups1 = new ArrayList<>();

        MemberGroup memberGroup = new MemberGroup(group,member1);
        memberGroupRepository.save(memberGroup);

        MemberGroup memberGroup2 = new MemberGroup(group,member2);
        memberGroupRepository.save(memberGroup2);

        memberGroups1.add(memberGroup);
        memberGroups1.add(memberGroup2);


        // 멤버 그룹2 생성
        List<MemberGroup> memberGroups2 = new ArrayList<>();

        MemberGroup memberGroup3 = new MemberGroup(group2,member3);
        memberGroupRepository.save(memberGroup3);

        MemberGroup memberGroup4 = new MemberGroup(group2,member4);
        memberGroupRepository.save(memberGroup4);

        memberGroups2.add(memberGroup3);
        memberGroups2.add(memberGroup4);

        //memberGroups 설정
        member1.setMemberGroups(memberGroups1);
        member2.setMemberGroups(memberGroups1);

        member3.setMemberGroups(memberGroups2);
        member4.setMemberGroups(memberGroups2);

        group.setMemberGroups(memberGroups1);
        group.setMemberGroups(memberGroups1);

        group2.setMemberGroups(memberGroups2);
        group2.setMemberGroups(memberGroups2);

        memberRepo.save(member1);
        memberRepo.save(member2);
        memberRepo.save(member3);
        memberRepo.save(member4);

        groupRepository.save(group);
        groupRepository.save(group2);
    }// end method

    @Test
    public void 그룹의_멤버확인(){
        Member findMember = memberRepo.findAll().get(0); //그룹1 의 멤버
        Member findMember2 = memberRepo.findAll().get(1); //그룹1 의 멤버
        Member findMember3 = memberRepo.findAll().get(2); //그룹2 의 멤버
        Member findMember4 = memberRepo.findAll().get(3); //그룹2 의 멤버

        Group group1 = groupRepository.findAll().get(0);
        Group group2 = groupRepository.findAll().get(1);

        //간단한 테스트라 print 해봄..
        System.out.println(findMember.getMemberGroups().get(0).getGroup().getGroupName());
        System.out.println(findMember2.getMemberGroups().get(0).getGroup().getGroupName());
        System.out.println(findMember3.getMemberGroups().get(0).getGroup().getGroupName());
        System.out.println(findMember4.getMemberGroups().get(0).getGroup().getGroupName());
    }

}
