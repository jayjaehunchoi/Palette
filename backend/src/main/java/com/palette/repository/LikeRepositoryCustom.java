package com.palette.repository;

import com.palette.domain.member.Member;

import java.util.List;

public interface LikeRepositoryCustom {
    public List<Member> findLikeMemberByPost(Long postId, Long likeId);
}
