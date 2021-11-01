package com.palette.dto;

import com.palette.domain.post.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// Story List에 단건마다 띄울 정보
@Getter
public class StoryListResponseDto {


    private Long memberId;
    private String memberName;
    private Long postId;
    private String thumbNailFullPath;
    private String title;
    private int likesCount;
    private PeriodDto period;

    public StoryListResponseDto(final Post post) {
        this.memberId = post.getMember().getId();
        this.memberName = post.getMember().getUname();
        this.postId = post.getId();
        this.thumbNailFullPath = post.getPhotos().get(0).getStoreFileName();
        this.title = post.getTitle();
        this.likesCount = post.getLikes().size();
        this.period = new PeriodDto(post.getPeriod().getStartDate(), post.getPeriod().getEndDate());
    }

    @QueryProjection
    @Builder
    public StoryListResponseDto(Long memberId, String memberName, Long postId, String thumbNailFullPath, String title, int likesCount, LocalDateTime startDate, LocalDateTime endDate) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.postId = postId;
        this.thumbNailFullPath = thumbNailFullPath;
        this.title = title;
        this.likesCount = likesCount;
        this.period = new PeriodDto(startDate,endDate);
    }

    public void setThumbNailFillPath(String path){
        this.thumbNailFullPath = path;
    }
}
