package com.palette.domain.member;

import javax.persistence.*;

import com.palette.domain.BaseTimeEntity;
import com.palette.domain.group.MemberGroup;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String upw;
    private String uname;
    private String profileFileName;
    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> memberGroups = new ArrayList<>();
    @Builder
    public Member(String upw, String uname, String profileFileName, String email) {
        this.upw = upw;
        this.uname = uname;
        this.profileFileName = profileFileName;
        this.email = email;
    }

    public Member update(String uname, String profileFileName) {
        this.uname = uname;
        this.profileFileName = profileFileName;

        return this;
    }

    public Member(String upw, String uname, String profileFileName,List<MemberGroup> memberGroups) {
        this.upw = upw;
        this.uname = uname;
        this.profileFileName = profileFileName;
        this.memberGroups = memberGroups;
    }

    public void addMemberGroups(MemberGroup memberGroups){
        //Member의 membergroups에 추가해주기
        this.memberGroups.add(memberGroups);
       memberGroups.getGroup().getMemberGroups().add(memberGroups);
    }
}