package com.palette.service;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import com.palette.dto.PeriodDto;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.repository.MemberRepository;
import com.palette.repository.PostGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostGroupServiceTest {

    @Autowired PostGroupService postGroupService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostGroupRepository postGroupRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp(){
        Member member = new Member("1234","재훈", "akds");
        Member member2 = new Member("1234","훈재", "akdsaa");

        memberRepository.save(member);
        memberRepository.save(member2);

        for(int i = 0 ; i < 15 ; i++){
            Member insertMember = member;
            String region = "서울";
            String title = "여행가자";
            if(i >= 5){
                insertMember = member2;
                region = "가평";
            }
            if(i > 5){
                title = "여행오자";
            }
            PostGroup postGroup = PostGroup.builder().member(insertMember).title(title).period(new Period(LocalDateTime.of(2021, 11, 01, 10, 10),
                    LocalDateTime.of(2021, 11, 03, 10, 10))).region(region).build();
            postGroupService.createPostGroup(postGroup);
        }
    }

    @Test
    void 멤버_필터_조회(){
        List<PostGroupResponseDto> responses = postGroupService.findPostGroupByMember("재훈", 1);
        assertThat(responses.size()).isEqualTo(5);
    }

    @Test
    void 지역_필터_조회(){
        List<PostGroupResponseDto> responses = postGroupService.findPostGroupByRegion("가평", 1);
        assertThat(responses.size()).isEqualTo(10);
    }

    @Test
    void 검색어_Like_조회(){
        List<PostGroupResponseDto> responses = postGroupService.findPostGroupByTitle("여행", 1);
        assertThat(responses.size()).isEqualTo(10);
        assertThat(responses.get(9).getTitle()).isEqualTo("여행가자");
        assertThat(responses.get(8).getTitle()).isEqualTo("여행오자");
    }

    @Test
    void 포스트그룹_업데이트(){
        PostGroup findGroup = postGroupService.findAll().get(0);

        postGroupService.updatePostGroup(findGroup.getId(), new PostGroupDto("여행이 좋아요", new PeriodDto(LocalDateTime.of(2021, 11, 01, 10, 10),
                LocalDateTime.of(2021, 11, 03, 10, 10)), "부산"));

        PostGroup findGroup2 = postGroupService.findById(findGroup.getId());

        assertThat(findGroup2.getTitle()).isEqualTo("여행이 좋아요");
        assertThat(findGroup2.getRegion()).isEqualTo("부산");
    }

    @Test
    void 포스트그룹_찾아오기(){
        PostGroup findGroup = postGroupService.findAll().get(0);
        assertThat(findGroup.getTitle()).isEqualTo("여행가자");
    }

    @AfterEach
    void tearDown(){
        System.out.println("==============After Each 실행=================");
        postGroupRepository.deleteAll();
        memberRepository.deleteAll();
    }

}