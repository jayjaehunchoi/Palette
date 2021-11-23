package com.palette.repository;

import com.palette.dto.response.CommentResponseDto;


import java.util.List;

public interface CommentRepositoryCustom{
    List<CommentResponseDto> findCommentByPostIdWithCursor(Long postId, Long commentId);
    List<CommentResponseDto> findChildCommentByCommentIdWithCursor(Long commentId, Long curCommentId);
}
