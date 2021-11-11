package com.palette.domain.member;

import javax.persistence.*;

import com.palette.domain.BaseTimeEntity;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.post.Post;
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
    private String name;
    private String password;
    private String profileFileName;
    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> memberGroups = new ArrayList<>();
    @Builder
    public Member(String name, String password, String profileFileName, String email) {
        this.name = name;
        this.password = password;
        this.profileFileName = profileFileName;
        this.email = email;
    }

    public Member update(String name, String profileFileName) {
        this.name = name;
        this.profileFileName = profileFileName;

        return this;
    }

    public Member(String name, String password, String profileFileName,List<MemberGroup> memberGroups) {
        this.name = name;
        this.password = password;
        this.profileFileName = profileFileName;
        this.memberGroups = memberGroups;
    }

    public void encodePassword(String encodedPassword) {
        password = encodedPassword;
    }
}