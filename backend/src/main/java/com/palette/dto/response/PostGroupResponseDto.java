package com.palette.dto.response;

import com.palette.dto.PeriodDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostGroupResponseDto {

    private Long postGroupId;
    private Long memberId;
    private String memberName;
    private String title;
    private String thumbNailUrl;
    private PeriodDto periodDto;
    private String region;
    private long totalLikes;

    public PostGroupResponseDto(Long postGroupId, Long memberId, String memberName, String title, String thumbNailUrl, LocalDateTime startDate, LocalDateTime endDate, String region) {
        this.postGroupId = postGroupId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.title = title;
        this.thumbNailUrl = thumbNailUrl;
        this.periodDto = new PeriodDto(startDate, endDate);
        this.region = region;
    }

    public void setTotalLikes(long likes){
        totalLikes =  likes;
    }
}
