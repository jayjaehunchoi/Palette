package com.palette.dto.response;

import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

//그룹 단건 조회 dto
@Getter
@NoArgsConstructor
public class GroupResponseDto {
    private Long id;
    private String groupCode;
    private String groupName;
    private String groupsIntroduction;
    private int numberOfPeople;

    private List<MemberResponseDto> responseDtoMembers = new ArrayList<>();

    public GroupResponseDto(Group group){
        this.id = group.getId();
        this.groupCode = group.getGroupCode();
        this.groupName = group.getGroupName();
        this.groupsIntroduction = group.getGroupsIntroduction();
        this.numberOfPeople = group.getNumberOfPeople();
    }

    public void setMembers(List<Member> members){
        for(int i = 0; i < members.size(); i++){
            responseDtoMembers.add(new MemberResponseDto(members.get(i)));
        }
    }
}
