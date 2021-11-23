package com.palette.repository;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.utils.constant.ConstantUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.palette.utils.constant.ConstantUtil.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PostGroupRepositoryTest {

    @Autowired PostGroupRepository postGroupRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    void 마이블로그_조회(){
        Member member = new Member("wogns","1234","wogns0108", "123");
        Member member2 = new Member("wogns11","1234","wogns0108", "1234");
        memberRepository.save(member);
        memberRepository.save(member2);

        List<Member> all = memberRepository.findAll();
        Member findMember = all.get(0);
        Member findMember2 = all.get(1);

        PostGroup postGroup = PostGroup.builder().member(findMember).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                , LocalDate.of(2021, 11, 5))).region("지역").build();

        PostGroup postGroup2 = PostGroup.builder().member(findMember2).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                , LocalDate.of(2021, 11, 5))).region("지역").build();
        postGroupRepository.save(postGroup);
        postGroupRepository.save(postGroup2);

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setName("wogns");

        // 조회 쿼리 2회
        List<PostGroupResponseDto> storyList = postGroupRepository.findStoryListWithPage(searchCondition, 1, 10);

        assertThat(storyList.size()).isEqualTo(1);
        assertThat(storyList.get(0).getMemberName()).isEqualTo("wogns");
    }

    @Test
    void 마이블로그_조건없는_조회(){
        Member member = new Member("wogns", "1234", "wogns","123");
        Member member2 = new Member("wogns11", "1234", "wogns","1234");
        memberRepository.save(member);
        memberRepository.save(member2);

        List<Member> all = memberRepository.findAll();
        Member findMember = all.get(0);
        Member findMember2 = all.get(1);

        PostGroup postGroup = PostGroup.builder().member(findMember).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                , LocalDate.of(2021, 11, 5))).region("지역").build();

        PostGroup postGroup2 = PostGroup.builder().member(findMember2).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                , LocalDate.of(2021, 11, 5))).region("지역").build();
        postGroupRepository.save(postGroup);
        postGroupRepository.save(postGroup2);

        SearchCondition searchCondition = new SearchCondition();

        // 조회 쿼리 2회
        List<PostGroupResponseDto> storyList = postGroupRepository.findStoryListWithPage(searchCondition, 1, 10);

        assertThat(storyList.size()).isEqualTo(2);
    }



    @Disabled
    @Test
    void 페이징_성능_확인(){

        Member member = new Member("wogns", "1234", "wogns","123");
        memberRepository.save(member);

        for(int i = 0 ; i < 10000; i++){
            PostGroup postGroup = PostGroup.builder().member(member).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                    , LocalDate.of(2021, 11, 5))).region("지역").build();
            postGroupRepository.save(postGroup);
        }
        SearchCondition searchCondition = new SearchCondition();
        long start = System.currentTimeMillis();
        List<PostGroupResponseDto> storyList = postGroupRepository.findStoryListWithPage(searchCondition, 1000, 10);
        long end = System.currentTimeMillis();

        System.out.println(" 1만건 기준 마지막 페이지 조회 성능 : " + (end-start) + "ms");
    }

    @Test
    void 페이징(){
        Member member = new Member("wogns", "1234", "wogns","123");
        memberRepository.save(member);
        for(int i = 0 ; i < 10; i++){
            PostGroup postGroup = PostGroup.builder().member(member).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                    , LocalDate.of(2021, 11, 5))).region("지역").build();
            postGroupRepository.save(postGroup);
        }
        List<PostGroupResponseDto> storyListWithPage = postGroupRepository.findStoryListWithPage(new SearchCondition(), 1, PAGE_SIZE);
        List<PostGroupResponseDto> storyListWithPage1 = postGroupRepository.findStoryListWithPage(new SearchCondition(), 2, PAGE_SIZE);

        assertThat(storyListWithPage.size()).isEqualTo(9);
        assertThat(storyListWithPage1.size()).isEqualTo(1);
    }

    @Test
    void 그룹_총개수(){
        Member member = new Member("wogns", "1234", "wogns","123");
        memberRepository.save(member);
        for(int i = 0 ; i < 100; i++){
            PostGroup postGroup = PostGroup.builder().member(member).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                    , LocalDate.of(2021, 11, 5))).region("지역").build();
            postGroupRepository.save(postGroup);
        }
        long count = postGroupRepository.getStoryListTotalCount(new SearchCondition());
        assertThat(count).isEqualTo(100);
    }

    @Test
    void 그룹_총개수_member(){
        Member member = new Member("wogns", "1234", "wogns","123");
        memberRepository.save(member);
        Member member2 = new Member("rlacl", "1234", "wogns","1234");
        memberRepository.save(member2);
        for(int i = 0 ; i < 10; i++){
            Member input = member;
            if(i > 5){
                input = member2;
            }
            PostGroup postGroup = PostGroup.builder().member(input).title("여행을 떠나요").period(new Period(LocalDate.of(2021, 11, 2)
                    , LocalDate.of(2021, 11, 5))).region("지역").build();
            postGroupRepository.save(postGroup);
        }
        SearchCondition condition = new SearchCondition();
        condition.setName("wogns");
        long count = postGroupRepository.getStoryListTotalCount(condition);
        assertThat(count).isEqualTo(6);
    }

}
