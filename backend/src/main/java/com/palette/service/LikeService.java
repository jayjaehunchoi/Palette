package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.domain.post.Like;
import com.palette.domain.post.Post;
import com.palette.exception.PostException;
import com.palette.repository.LikeRepository;
import com.palette.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public int pushLike(Member member, Long postId){
        Like findLike = likeRepository.findByMemberAndPostId(member, postId).orElse(null);
        Like likes = new Like(member);
        Post findPost = postRepository.findById(postId).orElse(null);
        if(findPost == null){
            throw new PostException("게시물이 존재하지 않습니다.");
        }
        if(findLike == null){
            likes.pushLike(findPost, false);
            return findPost.getLikeCount();
        }
        likes.pushLike(findPost, true);
        return findPost.getLikeCount();
    }

    // 커서페이징
    @Transactional(readOnly = true)
    public List<Member> findLikeMemberByPost(Long postId, Long likeId){
        return likeRepository.findLikeMemberByPost(postId,likeId);
    }

}
