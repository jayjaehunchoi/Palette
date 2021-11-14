package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class GroupJoinDto {
    @NotBlank(message="그룹 코드를 입력해주세요.")
    private String code;
}
