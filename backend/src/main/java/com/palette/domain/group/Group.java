package com.palette.domain.group;

import com.palette.dto.request.GroupUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Table(name = "travel_group",uniqueConstraints = @UniqueConstraint(name = "un_group_groupCode",columnNames = {"groupCode"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupCode;

    private String groupName;

    private String groupsIntroduction;

    private int numberOfPeople;

    @OneToOne
    @JoinColumn(name = "budget_id", foreignKey = @ForeignKey(name = "fk_travel_group_budget"))
    private Budget budget;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> memberGroups = new ArrayList<>();

    @Builder
    public Group(String groupName,String groupsIntroduction){
        this.groupName = groupName;
        this.groupsIntroduction = groupsIntroduction;
        this.numberOfPeople = 1;
        this.groupCode = createUUID();

    }

    public void setBudget(Budget budget){
        this.budget = budget;
    }

    private String createUUID() {
        String uuid = UUID.randomUUID().toString();
        String[] splitUUIDList = uuid.split("-");
        String splitUUID = splitUUIDList[1] + splitUUIDList[2] + splitUUIDList[3];
        String result = Base64.getEncoder().encodeToString(splitUUID.getBytes());
        return  result;
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
