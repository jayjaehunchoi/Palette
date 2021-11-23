package com.palette.dto.response;

import com.palette.domain.post.Like;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.List;

// Story List에 단건마다 띄울 정보
@NoArgsConstructor
@Getter
public class StoryListResponseDto {


    private Long memberId;
    private String memberName;
    private Long postId;
    private String thumbNailFullPath;
    private String title;
    private int likesCount;

    @QueryProjection
    @Builder
    public StoryListResponseDto(Long memberId, String memberName, Long postId ,String title, int likesCount) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.postId = postId;
        this.title = title;
        this.likesCount = likesCount;
    }

    public void setThumbNailFilePath(String path){
        this.thumbNailFullPath = path;
    }


}
