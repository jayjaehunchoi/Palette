package com.palette.repository;

import com.palette.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query("delete from Comment c where c.parentCommentId = :id")
    void deleteAllChildByIdInQuery(@Param("id") Long id);

}
