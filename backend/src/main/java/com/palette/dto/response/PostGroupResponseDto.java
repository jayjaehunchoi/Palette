package com.palette.dto.response;

import com.palette.domain.post.PostGroup;
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
    private PeriodDto period;
    private String region;

    public PostGroupResponseDto(final PostGroup postGroup) {
        this.postGroupId = postGroup.getId();
        this.memberId = postGroup.getMember().getId();
        this.memberName = postGroup.getMember().getName();
        this.title = postGroup.getTitle();
        this.thumbNailUrl = postGroup.getThumbNail().getStoreFileName();
        this.period = new PeriodDto(postGroup.getPeriod().getStartDate(), postGroup.getPeriod().getEndDate());
        this.region = postGroup.getRegion();
    }

    public PostGroupResponseDto(Long postGroupId, Long memberId, String memberName, String title, String thumbNailUrl, LocalDateTime startDate, LocalDateTime endDate, String region) {
        this.postGroupId = postGroupId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.title = title;
        this.thumbNailUrl = thumbNailUrl;
        this.period = new PeriodDto(startDate, endDate);
        this.region = region;
    }

}
