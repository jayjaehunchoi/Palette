package com.palette.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private List<MultipartFile> imageFiles;

}
