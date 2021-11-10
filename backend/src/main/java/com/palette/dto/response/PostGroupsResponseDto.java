package com.palette.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostGroupsResponseDto {

    List<PostGroupResponseDto> postGroupResponses = new ArrayList<>();

    @Builder
    public PostGroupsResponseDto(List<PostGroupResponseDto> postGroupResponses) {
        this.postGroupResponses = postGroupResponses;
    }
}
