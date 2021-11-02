package com.palette.dto.response;

import com.palette.domain.post.Like;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.List;

// Story List에 단건마다 띄울 정보
@Getter
public class StoryListResponseDto {


    private Long memberId;
    private String memberName;
    private Long postId;
    private Resource thumbNailFullPath;
    private String title;
    private long likesCount;

    @QueryProjection
    @Builder
    public StoryListResponseDto(Long memberId, String memberName, Long postId ,String title) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.postId = postId;
        this.title = title;

    }

    public void setThumbNailFilePath(Resource path){
        this.thumbNailFullPath = path;
    }
    public void setLikesCount(long likes){
        this.likesCount = likes;
    }

}
