package com.palette.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentDto {

    @NotBlank
    private String content;
    @NotBlank
    private Long postId;
    private Long commentId;
}
