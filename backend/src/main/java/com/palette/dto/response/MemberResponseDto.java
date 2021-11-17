package com.palette.dto.response;

import com.palette.domain.member.Member;
import com.palette.dto.Token;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long memberId;
    private String email;
    private String name;
    private String profile;
    private Token accessToken;

    public MemberResponseDto(final Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.profile = member.getProfileFileName();
    }

    public void setAccessToken(Token token){
        accessToken = token;
    }
}
