package com.palette.repository;

import com.palette.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from Post p where p.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
