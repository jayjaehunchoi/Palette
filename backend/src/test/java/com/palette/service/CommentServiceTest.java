package com.palette.service;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.Comment;
import com.palette.domain.post.Post;
import com.palette.exception.CommentException;
import com.palette.exception.PostException;
import com.palette.repository.MemberRepository;
import com.palette.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired MemberRepository memberRepository;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;

    @Test
    void 댓글_작성(){

        Member member = new Member("1234", "wogns", "wogns","123");
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);

        Comment comment = new Comment(member, "반가워요 우리 친하게 지내요");
        commentService.writeComment(comment, post.getId(), 0L);

        Comment findComment = commentService.findById(comment.getId());
        assertThat(findComment.getCommentContent()).isEqualTo("반가워요 우리 친하게 지내요");
        assertThat(findComment.getPost().getId()).isEqualTo(post.getId());
    }

    @Test
    void 댓글_작성중_게시판삭제_에러(){
        Member member = new Member("1234", "wogns", "wogns","123");
        memberRepository.save(member);
        Comment comment = new Comment(member, "반가워요 우리 친하게 지내요");

        assertThrows(PostException.class, () -> commentService.writeComment(comment,-1L,0L));
    }

    @Test
    void 댓글_업데이트(){
        Member member = new Member("1234", "wogns", "wogns","123");
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);
        Comment comment = new Comment(member, "반가워요 우리 친하게 지내요");
        commentService.writeComment(comment, post.getId(), 0L);

        commentService.updateComment(member.getId(),comment.getId(),"니가가라 하와이");
        Comment findComment = commentService.findById(comment.getId());
        assertThat(findComment.getCommentContent()).isEqualTo("니가가라 하와이");
    }

    @Test
    void 댓글_링크타고_부정방식으로_수정(){
        Member member = new Member("1234", "wogns", "wogns","123");
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);
        Comment comment = new Comment(member, "반가워요 우리 친하게 지내요");
        commentService.writeComment(comment, post.getId(), 0L);

        assertThrows(CommentException.class, () -> commentService.updateComment(member.getId(),-1L,"니가가라 하와이"));
    }

    @Test
    void 댓글_업데이트_권한_없음(){
        Member member = new Member("1234", "wogns", "wogns","123");
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);
        Comment comment = new Comment(member, "반가워요 우리 친하게 지내요");
        commentService.writeComment(comment, post.getId(), 0L);

        assertThrows(CommentException.class, () -> commentService.updateComment(-1L,comment.getId(),"니가가라 하와이"));

    }

    @Test
    void 댓글_삭제(){
        Member member = new Member("1234", "wogns", "wogns","123");
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("서울")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postService.write(post);
        Comment comment = new Comment(member, "반가워요 우리 친하게 지내요");
        commentService.writeComment(comment, post.getId(), 0L);

        commentService.deleteComment(member.getId(), comment.getId());
        assertThat(commentService.findById(comment.getId())).isEqualTo(null);
    }

    @AfterEach
    void tearDown(){
        System.out.println("=====================After Each=====================");
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
