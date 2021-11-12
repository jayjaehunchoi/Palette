package com.palette.dto.response;

import com.palette.domain.group.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//그룹 전체 조회 dto
@Getter
public class GroupsResponseDto {
    private List<GroupResponseDto> responseDtoGroups = new ArrayList<>();

    public void setResponseDtoGroups(List<Group> groups) {
        for(int i = 0; i < groups.size(); i++){
            responseDtoGroups.add(new GroupResponseDto(groups.get(i)));
        }
    }
}
