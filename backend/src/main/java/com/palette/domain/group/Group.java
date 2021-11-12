package com.palette.domain.group;

import com.palette.dto.request.GroupUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "travel_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long groupCode;

    private String groupName;

    private String groupsIntroduction;

    private int numberOfPeople;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> memberGroups = new ArrayList<>();

    @Builder
    public Group(String groupName,String groupsIntroduction){
        this.groupName = groupName;
        this.groupsIntroduction = groupsIntroduction;
        this.numberOfPeople = 1;
    }

    public void updateGroup(GroupUpdateDto dto){
        this.groupName = dto.getGroupName();
        this.groupsIntroduction = dto.getGroupIntroduction();
    }

    public void addNumberOfPeople(){
        this.numberOfPeople++;
    }

    public void reduceNumberOfPeople(){
        this.numberOfPeople--;
    }

}
