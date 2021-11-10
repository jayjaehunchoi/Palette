package com.palette.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class StoryListResponsesDto {

    List<StoryListResponseDto> storyLists = new ArrayList<>();

    @Builder
    public StoryListResponsesDto(List<StoryListResponseDto> storyLists) {
        this.storyLists = storyLists;
    }
}
