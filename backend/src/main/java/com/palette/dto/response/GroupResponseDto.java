package com.palette.dto.response;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class GroupResponseDto {
    private Long id;
    private long groupCode;
    private String groupName;
    private String groupsIntroduction;
    private int numberOfPeople;

    public GroupResponseDto(Group group){
        this.id = group.getId();
        this.groupCode = group.getGroupCode();
        this.groupName = group.getGroupName();
        this.groupsIntroduction = group.getGroupsIntroduction();
        this.numberOfPeople = group.getNumberOfPeople();
    }
    //private List<MemberGroup> memberGroups = new ArrayList<>();
}
