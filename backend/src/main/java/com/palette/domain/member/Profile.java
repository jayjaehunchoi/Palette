package com.palette.domain.member;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "member")
@Entity
@Table(name = "tbl_profile")
@EqualsAndHashCode(of = "fname")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    private String fname;

    private boolean current;

    @ManyToOne
    private Member member;
}