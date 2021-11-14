package com.palette.dto.response;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//그룹 전체 조회 dto
@Getter
public class GroupsResponseDto {
    private List<GroupResponseDto> responseDtoGroups = new ArrayList<>();

    public void setResponseDtoGroups(List<Group> groups) {
        for(int i = 0; i < groups.size(); i++){
            GroupResponseDto groupResponseDto = new GroupResponseDto(groups.get(i));
            List<Member> members = getMembers(groups.get(i));
            groupResponseDto.setMembers(members);
            responseDtoGroups.add(groupResponseDto);
        }
    }

    //memberGroups에서 각각 멤버들 가져와서 member 리스트에 넣기
    private List<Member> getMembers(Group group) {
        List<Member> members = new ArrayList<>();
        List<MemberGroup> memberGroups = group.getMemberGroups();
        for(int i = 0 ; i < memberGroups.size(); i++){
            members.add(memberGroups.get(i).getMember());
        }
        return members;
    }
}
