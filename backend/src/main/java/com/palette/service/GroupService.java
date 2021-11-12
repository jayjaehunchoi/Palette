package com.palette.service;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.GroupUpdateDto;
import com.palette.exception.GroupException;
import com.palette.repository.GroupRepository;
import com.palette.repository.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;

    //그룹 조회
    @Transactional
    public Group findById(Long id){
        Group findGroup = groupRepository.findById(id).orElse(null);
        isGroupExist(findGroup);
        return findGroup;
    }

    //그룹 최초 추가
    @Transactional
    public void addGroup(Group group, Member member){
        groupRepository.save(group);
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.addMemberGroup(group,member);
    }

    //그룹에 멤버 추가
    @Transactional
    public void addGroupMember(String groupCode,Member member){
        Group findGroup = groupRepository.findGroupByGroupCode(groupCode).orElse(null);
        isGroupExist(findGroup);
        isAlreadyJoin(member,findGroup);

        MemberGroup memberGroup = new MemberGroup();
        memberGroupRepository.save(memberGroup);
        memberGroup.addMemberGroup(findGroup,member);

        findGroup.addNumberOfPeople();
    }

    //그룹 멤버 삭제
    @Transactional
    public void deleteGroupMember(Long id,Member member){
        Group findGroup = groupRepository.findById(id).orElse(null);
        isGroupExist(findGroup);
        isMemberDeleted(findGroup);
        MemberGroup findMemberGroup = memberGroupRepository.findByMemberAndGroup(member,findGroup);
        findMemberGroup.deleteMemberGroup(findGroup,member);

        findGroup.reduceNumberOfPeople();

    }

    //그룹 업데이트
    @Transactional
    public void updateGroup(Long id, GroupUpdateDto dto){
        Group findGroup = groupRepository.findById(id).orElse(null);
        isGroupExist(findGroup);
        findGroup.updateGroup(dto);
    }

    //그룹삭제
    @Transactional
    public void deleteGroup(Long id){
        Group findGroup = groupRepository.findById(id).orElse(null);
        isGroupExist(findGroup);
        groupRepository.deleteById(id);
    }

    //그룹이 존재하는지 확인
    private void isGroupExist(Group findGroup) {
        if (findGroup == null) {
            log.error("Group Not Exist Error");
            throw new GroupException("존재하지 않는 그룹입니다.");
        }
    }

    //이미 그룹에 해당 멤버가 존재하는지 확인 todo: 테스트해보기
    private void isAlreadyJoin(Member member, Group group){
        List<MemberGroup> memberGroups = group.getMemberGroups();
        for(int i = 0; i < memberGroups.size(); i++){
            if(member == memberGroups.get(i).getMember()){
                throw new GroupException("이미 가입된 그룹입니다.");
            }
        }
    }

    //그룹의 멤버가 1명 남았을때는 멤버 삭제 불가능
    private void isMemberDeleted(Group group){
        if(group.getNumberOfPeople() == 1) {
            throw new GroupException("그룹에 남은 유일한 멤버라 탈퇴 불가능합니다.");
        }
    }

}
