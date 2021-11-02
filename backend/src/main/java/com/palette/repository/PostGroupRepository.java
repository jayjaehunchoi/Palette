package com.palette.repository;

import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostGroupRepository extends JpaRepository<PostGroup, Long> {
    List<PostGroup> findByRegion(String region);
    Optional<List<PostGroup>> findByMember(Member member);

}
