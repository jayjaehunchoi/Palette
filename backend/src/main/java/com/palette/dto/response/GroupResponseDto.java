package com.palette.dto.response;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//그룹 단건 조회 dto
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
