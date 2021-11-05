package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

// todo : update 전용 dto, 어떤 내용까지 update할지 의사결정 필요
@NoArgsConstructor
@Getter
public class PostUpdateDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public PostUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
