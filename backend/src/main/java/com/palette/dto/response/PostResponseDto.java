package com.palette.dto.response;

import com.palette.domain.post.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {

    private Long memberId;
    private String memberName;
    private Long postId;
    private String postTitle;
    private String postContent;
    private int likes;
    private int hit;
    private List<String> images;
    private List<CommentResponseDto> comments;

    public PostResponseDto(final Post post){
        this.memberId = post.getMember().getId();
        this.memberName = post.getMember().getName();
        this.postId = post.getId();
        this.postTitle = post.getTitle();
        this.postContent = post.getContent();
        this.likes = post.getLikeCount();
        this.hit = post.getHit();
    }

    public void setComments(List<CommentResponseDto> commentResponseDtos) {
        this.comments = commentResponseDtos;
    }

    public void setImages(List<String> imagesFullPath){
        this.images = imagesFullPath;
    }

}
