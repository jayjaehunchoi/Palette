package com.palette.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long memberId;
    private String memberName;
    private Long commentId;
    private String commentContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    public CommentResponseDto(Long memberId, String memberName, Long commentId, String commentContent, LocalDateTime createDate) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.createDate = createDate;
    }
}
