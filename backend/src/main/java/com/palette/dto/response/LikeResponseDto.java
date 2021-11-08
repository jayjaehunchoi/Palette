package com.palette.dto.response;

import com.palette.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LikeResponseDto {

    private Long memberId;
    private String memberName;

    public LikeResponseDto(final Member member) {
        this.memberId = member.getId();
        this.memberName = member.getName();
    }
}
