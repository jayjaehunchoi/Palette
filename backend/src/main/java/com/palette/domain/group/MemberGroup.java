package com.palette.domain.group;

import com.palette.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MemberGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMemberGroup(Group group, Member member){
        this.group = group;
        this.member = member;
        group.getMemberGroups().add(this);
        member.getMemberGroups().add(this);
    }

    public void deleteMemberGroup(Group group, Member member){
        this.group = group;
        this.member = member;
        group.getMemberGroups().remove(this);
        member.getMemberGroups().remove(this);
    }
}
