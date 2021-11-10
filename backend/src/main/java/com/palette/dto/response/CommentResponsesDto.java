package com.palette.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CommentResponsesDto {

    List<CommentResponseDto> commentResponses = new ArrayList<>();


    @Builder
    public CommentResponsesDto(List<CommentResponseDto> commentResponses) {
        this.commentResponses = commentResponses;
    }
}
