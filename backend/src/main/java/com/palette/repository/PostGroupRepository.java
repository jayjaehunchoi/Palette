package com.palette.repository;


import com.palette.domain.post.PostGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostGroupRepository extends JpaRepository<PostGroup, Long>, PostGroupRepositoryCustom {
}
