package com.palette.config;

import com.palette.domain.member.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String uname;
    private String email;
    private String profileFileName;

    public SessionUser(Member member) {
        this.uname = member.getUname();
        this.email = member.getEmail();
        this.profileFileName = member.getProfileFileName();
    }
}
