package com.palette.dto.request;

import com.palette.domain.group.MemberGroup;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupUpdateDto {
    @NotBlank(message = "그룹 이름을 입력해주세요.")
    private String groupName;

    @NotBlank(message = "그룹 정보를 입력해주세요.")
    private String groupIntroduction;

}
