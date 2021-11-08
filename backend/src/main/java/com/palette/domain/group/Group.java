package com.palette.domain.group;

import com.palette.dto.request.GroupUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    private String groupIntroduction;

    private int numberOfPeople;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> memberGroups = new ArrayList<>();

    @Builder
    public Group(String groupName,String groupIntroduction){
        this.groupName = groupName;
        this.groupIntroduction = groupIntroduction;
        this.numberOfPeople = 1;
    }

    //todo : membergroup 업데이트 이렇게 하는지 맞낭..
    public void updateGroup(GroupUpdateDto dto){
        this.groupName = dto.getGroupName();
        this.groupIntroduction = dto.getGroupIntroduction();
        this.numberOfPeople = dto.getNumberOfPeople();
    }
}
