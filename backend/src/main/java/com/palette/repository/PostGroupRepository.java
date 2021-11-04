package com.palette.repository;

import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostGroupRepository extends JpaRepository<PostGroup, Long>, PostGroupRepositoryCustom {
}
