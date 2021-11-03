package com.palette.dto.request;

import com.palette.dto.PeriodDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostGroupDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private PeriodDto periodDto;
    @NotBlank(message = "지역정보를 입력해주세요.")
    private String region;

    @Builder
    public PostGroupDto(String title, PeriodDto periodDto, String region) {
        this.title = title;
        this.periodDto = periodDto;
        this.region = region;
    }
}
