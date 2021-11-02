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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> memberGroups = new ArrayList<>();

    public Member(String upw, String uname, String profileFileName) {
        this.upw = upw;
        this.uname = uname;
        this.profileFileName = profileFileName;
    }
}