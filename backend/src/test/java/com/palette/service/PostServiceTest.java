package com.palette.service;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.Like;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.repository.LikeRepository;
import com.palette.repository.MemberRepository;
import com.palette.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@Transactional
public class PostServiceTest {

    @Autowired PostService postService;
    @Autowired LikeService likeService;
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

        likeService.pushLike(findMember,post.getId());

        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.get(0).getLikesCount()).isEqualTo(1);
    }

    @Test
    void 좋아요_더블클릭_취소(){
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
        likeService.pushLike(findMember,post.getId());
        likeService.pushLike(findMember,post.getId());
        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.get(0).getLikesCount()).isEqualTo(0);

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

        List<MyFile> myFiles = new ArrayList<>();
        myFiles.add(new MyFile("ab.jpg","ab.jpg"));
        postService.write(post,myFiles);


        Post post2 = Post.builder().title("제목입니다")
                .member(findMember)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        List<MyFile> myFiles2 = new ArrayList<>();
        myFiles2.add(new MyFile("abc.jpg","abc.jpg"));
        myFiles2.add(new MyFile("abcd.jpg","abcd.jpg"));
        postService.write(post2,myFiles2);


        List<StoryListResponseDto> storyList = postService.findStoryList(new SearchCondition(), 1, 10);
        assertThat(storyList.get(1).getThumbNailFullPath().getFilename()).isEqualTo("ab.jpg");
        assertThat(storyList.get(0).getThumbNailFullPath().getFilename()).isEqualTo("abc.jpg");
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
