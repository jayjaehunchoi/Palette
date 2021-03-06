package com.palette.service;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import com.palette.dto.PeriodDto;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.repository.MemberRepository;
import com.palette.repository.PostGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
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
        Member member = new Member("재훈","1234", "akds", "123");
        Member member2 = new Member("훈재","1234", "akdsaa", "1234");

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
            if(i == 14){
                title = "여행오자";
            }
            PostGroup postGroup = PostGroup.builder().member(insertMember).title(title).period(new Period(LocalDate.of(2021, 11, 01),
                    LocalDate.of(2021, 11, 03))).region(region).build();
            postGroupService.createPostGroup(postGroup);
        }
    }

    @Test
    void 멤버_필터_조회(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setName("재훈");
        List<PostGroupResponseDto> responses = postGroupService.findPostGroup(searchCondition, 1);
        assertThat(responses.size()).isEqualTo(5);
    }

    @Test
    void 지역_필터_조회(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setRegion("가평");
        List<PostGroupResponseDto> responses = postGroupService.findPostGroup(searchCondition, 1);
        assertThat(responses.size()).isEqualTo(9);
    }

    @Test
    void 검색어_Like_조회(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setTitle("여행");
        List<PostGroupResponseDto> responses = postGroupService.findPostGroup(searchCondition, 1);
        assertThat(responses.size()).isEqualTo(9);
        assertThat(responses.get(8).getTitle()).isEqualTo("여행가자");
        assertThat(responses.get(0).getTitle()).isEqualTo("여행오자");
    }

    @Test
    void Id_필터_조회(){
        Long id = memberRepository.findAll().get(0).getId();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setMemberId(id);
        List<PostGroupResponseDto> postGroup = postGroupService.findPostGroup(searchCondition, 1);
        assertThat(postGroup.size()).isEqualTo(5);
    }

    @Test
    void 포스트그룹_업데이트(){
        PostGroup findGroup = postGroupService.findAll().get(0);


        postGroupService.updatePostGroup(findGroup.getId(), new PostGroupDto("여행이 좋아요", new PeriodDto(LocalDate.of(2021, 11, 02),
                LocalDate.of(2021, 11, 03)), "부산"), null);


        PostGroup findGroup2 = postGroupService.findById(findGroup.getId());

        assertThat(findGroup2.getTitle()).isEqualTo("여행이 좋아요");
        assertThat(findGroup2.getRegion()).isEqualTo("부산");
        assertThat(findGroup2.getPeriod().getStartDate()).isEqualTo((LocalDate.of(2021, 11, 02)));
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