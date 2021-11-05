package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CommentDto {

    @NotBlank
    private String content;
    @NotBlank
    private Long postId;
    private Long commentId;
}
