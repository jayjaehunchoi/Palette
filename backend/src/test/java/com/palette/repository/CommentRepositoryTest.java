package com.palette.repository;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.Comment;
import com.palette.domain.post.Post;
import com.palette.dto.response.CommentResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class CommentRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;

    @BeforeEach
    void setUp(){
        Member member = new Member("1234","wogns","wogns0108", "123");
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다")
                .member(member)
                .content("내용")
                .region("지역")
                .period(new Period(LocalDateTime.of(2021, 11, 2, 20, 20)
                        , LocalDateTime.of(2021, 11, 5, 20, 20)))
                .build();
        postRepository.save(post);

        for(int i = 0 ; i < 15; i++){
            Comment comment = new Comment(member, "안녕"+i);
            comment.writeComment(post,0L);
        }

        Long parentId = commentRepository.findCommentByPostIdWithCursor(post.getId(), null).get(0).getCommentId();
        for(int i = 0 ; i < 15; i++){
            Comment comment = new Comment(member, "답글"+i);
            comment.writeComment(post,parentId);
        }

    }

    @Test
    void 최초_게시물접근_댓글조회(){
        Post post = postRepository.findAll().get(0);
        List<CommentResponseDto> responseDto = commentRepository.findCommentByPostIdWithCursor(post.getId(), null);

        assertThat(responseDto.size()).isEqualTo(10);
        assertThat(responseDto.get(0).getCommentContent()).isEqualTo("안녕0");
    }

    @Test
    void 댓글_더보기_클릭(){
        Post post = postRepository.findAll().get(0);
        List<CommentResponseDto> responseDto = commentRepository.findCommentByPostIdWithCursor(post.getId(), null);

        List<CommentResponseDto> res = commentRepository.findCommentByPostIdWithCursor(post.getId(), responseDto.get(responseDto.size() - 1).getCommentId());

        assertThat(res.size()).isEqualTo(5);
        assertThat(res.get(0).getCommentContent()).isEqualTo("안녕10");
    }


    @Test
    void 답글_조회(){
        Post post = postRepository.findAll().get(0);
        Long parId = commentRepository.findAll().get(0).getId();
        List<CommentResponseDto> responseDto = commentRepository.findChildCommentByCommentIdWithCursor(parId, null);
        assertThat(responseDto.size()).isEqualTo(10);
        assertThat(responseDto.get(0).getCommentContent()).isEqualTo("답글0");

    }

    @Test
    void 답글_더보기_클릭(){
        Post post = postRepository.findAll().get(0);
        Long parId = commentRepository.findAll().get(0).getId();
        List<CommentResponseDto> responseDto = commentRepository.findChildCommentByCommentIdWithCursor(parId, null);

        List<CommentResponseDto> res = commentRepository.findChildCommentByCommentIdWithCursor(parId, responseDto.get(responseDto.size() - 1).getCommentId());
        assertThat(res.size()).isEqualTo(5);
        assertThat(res.get(0).getCommentContent()).isEqualTo("답글10");
    }

}
