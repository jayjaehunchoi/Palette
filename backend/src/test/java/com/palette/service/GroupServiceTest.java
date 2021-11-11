package com.palette.service;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.repository.BudgetRepository;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import com.palette.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class GroupServiceTest {
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberGroupRepository memberGroupRepository;
    @Autowired
    BudgetRepository budgetRepository;
    @Autowired
    GroupService groupService;

    @BeforeEach
    public void setUp() {

        // 회원생성
        Member member1 = new Member("wltn", "1234", "wogns","123");
        memberRepo.save(member1);

        Member member2 = new Member("skfk", "1234", "wogns","123");
        memberRepo.save(member2);

        Member member3 = new Member("wognswognwosgns", "1234", "wogns","123");
        memberRepo.save(member3);

        Member member4 = new Member("ruddls", "1234", "wogns","123");
        memberRepo.save(member4);

        // 그룹생성
        Group group = Group.builder().
                groupName("그룹1").
                groupsIntroduction("테스트 그룹 1입니다.")
                .build();
        groupRepository.save(group);

        Group group2 = Group.builder()
                .groupName("그룹2")
                .groupsIntroduction("테스트 그룹 2입니다")
                .build();
        groupRepository.save(group2);

        // 멤버 그룹 생성 후 member에 추가해주기
        // member1은 group과 group2둘 다 들어감
        MemberGroup memberGroup = new MemberGroup();
        memberGroupRepository.save(memberGroup);
        memberGroup.addMemberGroup(group,member1);

        MemberGroup memberGroup1 = new MemberGroup();
        memberGroupRepository.save(memberGroup1);
        memberGroup1.addMemberGroup(group2,member1);

        MemberGroup memberGroup2 = new MemberGroup();
        memberGroupRepository.save(memberGroup2);
        memberGroup2.addMemberGroup(group,member2);

        MemberGroup memberGroup3 = new MemberGroup();
        memberGroupRepository.save(memberGroup3);
        memberGroup3.addMemberGroup(group2,member3);

        MemberGroup memberGroup4 = new MemberGroup();
        memberGroupRepository.save(memberGroup4);
        memberGroup4.addMemberGroup(group2,member4);

        // group : member1, member2
        // group2 : member1, member3, member4

        System.out.println("-------------setUp end-------------");
    }// end method

    @Test
    public void 그룹_최초생성(){
        // todo : groupDto에서 받아와서 group 객체에 담아주기, memberDto도 마찬가지..
        Group group = new Group("청춘은바로지금","우리의 태국 여행~");
        Member findMember = memberRepo.findAll().get(0);
        groupService.addGroup(group,findMember);

        assertThat(groupRepository.findAll().get(2)).isEqualTo(group);
    }

    @Test
    public void 그룹의_멤버확인(){
        Member findMember = memberRepo.findAll().get(0); //그룹1 의 멤버
        Member findMember2 = memberRepo.findAll().get(1); //그룹1 의 멤버
        Member findMember3 = memberRepo.findAll().get(2); //그룹2 의 멤버
        Member findMember4 = memberRepo.findAll().get(3); //그룹2 의 멤버

        Group group1 = groupRepository.findAll().get(0);
        Group group2 = groupRepository.findAll().get(1);

        assertThat(findMember.getMemberGroups().get(0).getGroup()).isEqualTo(group1);
        assertThat(findMember.getMemberGroups().get(1).getGroup()).isEqualTo(group2);
        assertThat(findMember2.getMemberGroups().get(0).getGroup()).isEqualTo(group1);
        assertThat(findMember3.getMemberGroups().get(0).getGroup()).isEqualTo(group2);
        assertThat(findMember4.getMemberGroups().get(0).getGroup()).isEqualTo(group2);
    }

    @Test
    public void 그룹에_예산_넣기(){
        Group findGroup = groupRepository.findAll().get(0);
        Budget budget = new Budget(findGroup,100000L);
        budgetRepository.save(budget);

        assertThat(budgetRepository.findById(budget.getId()).orElse(null).getGroup()).isEqualTo(findGroup);
    }

    @Test
    public void 그룹_멤버_삭제(){
        Member member = memberRepo.findAll().get(0);
        Group findGroup = groupRepository.findAll().get(0);

        MemberGroup findMemberGroup = memberGroupRepository.findByMemberAndGroup(member,findGroup);
        findMemberGroup.deleteMemberGroup(findGroup,member);

        assertThat(member.getMemberGroups().get(0).getGroup()).isEqualTo(groupRepository.findAll().get(1)); //group2만 남아있어야하는 상태
    }

    //todo: 그룹업데이트 테스트

   @AfterEach
    void 전체_삭제(){
        System.out.println("-------------AfterEach-------------");
        memberRepo.deleteAll();
        groupRepository.deleteAll();
    }
}
