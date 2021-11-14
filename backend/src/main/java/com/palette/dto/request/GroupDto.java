package com.palette.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class GroupDto {

    private long groupId;

    @NotBlank(message = "그룹 이름을 입력해주세요.")
    private String groupName;

    @NotBlank(message = "그룹 정보를 입력해주세요.")
    private String groupIntroduction;

    public GroupDto(long groupId, String groupName, String groupIntroduction) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupIntroduction = groupIntroduction;
    }
}
