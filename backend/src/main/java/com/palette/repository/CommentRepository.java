package com.palette.repository;

import com.palette.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Transactional
    @Modifying
    @Query("delete from Comment c where c.parentCommentId = :id")
    void deleteAllChildById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("delete from Comment c where c.post.id = :id")
    void deleteAllCommentByPostId(@Param("id") Long id);

}
