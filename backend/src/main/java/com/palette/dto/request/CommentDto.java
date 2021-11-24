package com.palette.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CommentDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Length(min = 0, max = 120, message = "0 ~ 20자 사이로 입력해주세요.")
    private String content;

    @Builder
    public CommentDto(String content) {
        this.content = content;
    }
}
