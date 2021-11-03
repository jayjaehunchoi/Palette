package com.palette.dto;

import lombok.Getter;

// todo : update 전용 dto, 어떤 내용까지 update할지 의사결정 필요
@Getter
public class PostUpdateDto {

    private String title;
    private String content;

    public PostUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
