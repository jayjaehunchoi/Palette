package com.palette.repository;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.Like;
import com.palette.domain.post.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LikeRepositoryTest {
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired LikeRepository likeRepository;

    @Test
    void 좋아요_멤버_조회(){
        Member member = new Member("wogns", "1234", "wogns","123");
        memberRepository.save(member);
        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("부산")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postRepository.save(post);

        Like like = new Like(member);
        like.pushLike(post,false);

        List<Member> findMember = likeRepository.findLikeMemberByPost(post.getId(), null);


    }
}
