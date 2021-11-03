package com.palette.dto.response;

import com.palette.dto.request.CommentDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {

    private String postTitle;
    private String postContent;
    private long likes;
    private List<CommentDto> commentDtos;



}
