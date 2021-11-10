package com.palette.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CommentDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentDto(String content) {
        this.content = content;
    }
}
