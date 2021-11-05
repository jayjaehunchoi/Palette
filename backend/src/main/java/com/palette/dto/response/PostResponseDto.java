package com.palette.dto.response;

import com.palette.domain.post.Post;
import com.palette.dto.request.CommentDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {

    private Long memberId;
    private String memberName;
    private String postTitle;
    private String postContent;
    private int likes;
    private List<String> images;
    private List<CommentResponseDto> commentResponseDtos;

    public PostResponseDto(final Post post){
        this.memberId = post.getMember().getId();
        this.memberName = post.getMember().getUname();
        this.postTitle = post.getTitle();
        this.postContent = post.getContent();
        this.likes = post.getLikeCount();
    }

    public void setComments(List<CommentResponseDto> commentResponseDtos) {
        this.commentResponseDtos = commentResponseDtos;
    }

    public void setImages(List<String> imagesFullPath){
        this.images = imagesFullPath;
    }

}
