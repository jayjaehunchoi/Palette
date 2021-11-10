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
    @Column(name = "member_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",foreignKey = @ForeignKey(name = "fk_member_group_travel_group"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_member_group_member"))
    private Member member;

    public void addMemberGroup(Group group, Member member){
        this.group = group;
        this.member = member;
        group.getMemberGroups().add(this);
        member.getMemberGroups().add(this);

        // todo: 양방향 참조가 아닌 일방향 참조 생각해보기
    }

    public void deleteMemberGroup(Group group, Member member){
        this.group = group;
        this.member = member;
        group.getMemberGroups().remove(this);
        member.getMemberGroups().remove(this);
    }
}
