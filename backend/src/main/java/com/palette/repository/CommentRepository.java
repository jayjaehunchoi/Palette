package com.palette.repository;

import com.palette.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Modifying
//    @Query("delete from Post p where p.id in :ids")
//    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
