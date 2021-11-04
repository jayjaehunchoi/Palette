package com.palette.repository;

import com.palette.domain.post.Comment;
import com.palette.dto.response.CommentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepositoryCustom{
    List<CommentResponseDto> findCommentByPostIdWithCursor(Long postId, Long commentId);
    List<CommentResponseDto> findChildCommentByCommentIdWithCursor(Long commentId);
}
