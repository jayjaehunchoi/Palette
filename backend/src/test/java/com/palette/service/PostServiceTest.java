package com.palette.service;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.Like;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.palette.dto.SearchCondition;
import com.palette.dto.StoryListResponseDto;
import com.palette.repository.LikeRepository;
import com.palette.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired PostService postService;
    @Autowired
    LikeRepository likeRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    void 좋아요_개수_조회(){
        Member member = new Member("1234", "wogns", "wogns");
        memberRepository.save(member);
        Member findMember = memberRepository.findAll().get(0);

        Post post = Post.builder().title("제목입니다")
                .member(findMember)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);

        likeRepository.save(new Like(findMember,post));

        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.get(0).getLikesCount()).isEqualTo(1);
    }

    @Test
    void 기본_썸네일_조회(){
        Member member = new Member("1234", "wogns", "wogns");
        memberRepository.save(member);
        Member findMember = memberRepository.findAll().get(0);

        Post post = Post.builder().title("제목입니다")
                .member(findMember)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);

        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.get(0).getThumbNailFullPath().getFilename()).isEqualTo("기본썸네일");
    }

    @Test
    void 지정_썸네일_조회(){
        Member member = new Member("1234", "wogns", "wogns");
        memberRepository.save(member);
        Member findMember = memberRepository.findAll().get(0);

        Post post = Post.builder().title("제목입니다")
                .member(findMember)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        post.getPhotos().add(new Photo("ab.jpg","ab.jpg",post));
        postService.write(post);

        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.get(0).getThumbNailFullPath().getFilename()).isEqualTo("ab.jpg");

    }

    @Test
    void 복수_쿼리_확인(){
        Member member = new Member("1234", "wogns", "wogns");
        memberRepository.save(member);
        Member findMember = memberRepository.findAll().get(0);

        for(int i = 0 ; i < 10 ; i++){
            String region = "서울";
            Post post = Post.builder().title("제목입니다" + i)
                    .member(findMember)
                    .content("내용")
                    .region(region)
                    .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                            , LocalDateTime.of(2021, 11, 5, 20, 20)))
                    .build();

            postService.write(post);
        }

        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.size()).isEqualTo(10);
    }

}
