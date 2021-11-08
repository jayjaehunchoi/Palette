package com.palette.dto.request;

import com.palette.domain.group.MemberGroup;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupUpdateDto {
    @NotBlank//todo:메세지 적기 max값도 설정
    private String groupName;

    @NotBlank
    private String groupIntroduction;

}
