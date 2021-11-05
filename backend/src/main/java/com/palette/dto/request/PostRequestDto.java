package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private List<MultipartFile> imageFiles;

}
