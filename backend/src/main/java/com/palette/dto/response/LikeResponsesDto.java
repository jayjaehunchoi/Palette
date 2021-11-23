package com.palette.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class LikeResponsesDto {

    List<LikeResponseDto> likeResponses = new ArrayList<>();

    @Builder
    public LikeResponsesDto(List<LikeResponseDto> likeResponses) {
        this.likeResponses = likeResponses;
    }
}
