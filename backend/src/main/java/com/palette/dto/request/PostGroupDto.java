package com.palette.dto.request;

import com.palette.dto.PeriodDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class PostGroupDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 0, max = 20, message = "0 ~ 20자 사이로 입력해주세요.")
    private String title;
    private PeriodDto period;
    @NotBlank(message = "지역정보를 입력해주세요.")
    private String region;

    @Builder
    public PostGroupDto(String title, PeriodDto periodDto, String region) {
        this.title = title;
        this.period = periodDto;
        this.region = region;
    }
}
