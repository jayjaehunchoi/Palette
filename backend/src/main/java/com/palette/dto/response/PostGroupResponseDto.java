package com.palette.dto.response;

import com.palette.dto.PeriodDto;
import lombok.Getter;

@Getter
public class PostGroupResponseDto {

    private Long postGroupId;
    private Long memberId;
    private String memberName;
    private String title;
    private String thumbNailUrl;
    private PeriodDto periodDto;
    private String region;


    public PostGroupResponseDto(Long postGroupId, Long memberId, String memberName, String title, String thumbNailUrl, PeriodDto periodDto, String region) {
        this.postGroupId = postGroupId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.title = title;
        this.thumbNailUrl = thumbNailUrl;
        this.periodDto = periodDto;
        this.region = region;
    }
}
