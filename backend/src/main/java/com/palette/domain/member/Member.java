package com.palette.domain.member;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name="tbl_members")
@EqualsAndHashCode(of="uid")
public class Member {

    @Id
    private String uid;
    private String upw;
    private String uname;

}