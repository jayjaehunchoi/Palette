package com.palette.repository;

import com.palette.domain.member.Member;
import com.palette.domain.post.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
    Optional<Like> findByMemberAndPostId(Member member, Long postId);
    @Transactional
    @Modifying
    @Query("delete from Like l where l.post.id = :id")
    void deleteAllLikeByPostId(@Param("id") Long id);
}
