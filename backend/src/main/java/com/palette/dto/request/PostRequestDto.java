package com.palette.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 0, max = 20, message = "0 ~ 20자 사이로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Length(min = 0, max = 150, message = "0 ~ 150자 사이로 입력해주세요.")
    private String content;

    @Builder
    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
