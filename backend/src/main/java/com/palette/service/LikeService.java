package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.domain.post.Like;
import com.palette.domain.post.Post;
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
    public void pushLike(Member member, Long postId){
        Like findLike = likeRepository.findByMemberAndPostId(member, postId).orElse(null);
        Like likes = new Like(member);
        Post findPost = postRepository.findById(postId).orElse(null);
        if(findLike == null){
            likes.pushLike(findPost, false);
            return;
        }
        likes.pushLike(findPost, true);
    }

    @Transactional
    public void deleteLike(Long id){
        likeRepository.deleteById(id);
    }

    // 커서페이징
    @Transactional(readOnly = true)
    public List<Member> findLikeMemberByPost(Long postId, Long likeId){
        return likeRepository.findLikeMemberByPost(postId,likeId);
    }

}
