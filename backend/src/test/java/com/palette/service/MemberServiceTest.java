package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.dto.request.MemberUpdateDto;
import com.palette.exception.MemberException;
import com.palette.repository.MemberRepository;
import com.palette.utils.S3Uploader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired SendEmailService sendEmailService;
    @MockBean
    S3Uploader s3Uploader;

    @Test
    void 회원가입() {
        Member member = new Member("wogns", "1234", "wogns","123");
        memberService.signUp(member);

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);
        boolean matches = passwordEncoder.matches("1234", members.get(0).getPassword());
        assertTrue(matches);
    }

    @Test
    void 로그인_성공(){
        Member member = new Member("wogns", "1234", "w123","wogns@naver.com");
        memberService.signUp(member);
        Member successMember = memberService.logIn("wogns@naver.com", "1234");
        assertThat(member.getId()).isEqualTo(successMember.getId());
    }

    @Test
    void 로그인_실패(){
        Member member = new Member("wogns", "1234", "1234","wogns@naver.com");
        memberService.signUp(member);
        assertThrows(MemberException.class, ()-> memberService.logIn("wogns@naver.com", "123"));
    }

    @Test
    void 마이페이지_수정(){
        Member member = new Member("wogns", "1234", "1234","wogns@naver.com");
        memberService.signUp(member);
        BDDMockito.doNothing().when(s3Uploader).deleteS3(Arrays.asList("1234"));
        memberService.updateMember(member.getId(),new MemberUpdateDto("123456"),"abcd");

        Member findMember = memberRepository.findAll().get(0);
        assertTrue(passwordEncoder.matches("123456",findMember.getPassword()));
        assertThat(findMember.getProfileFileName()).isEqualTo("abcd");
    }

    @Test
    void 회원_정보_가져오기(){
        Member member = new Member("wogns", "1234", "1234","wogns@naver.com");
        memberService.signUp(member);

        Member memberInfo = memberService.getMemberInfo(member.getId());

        assertThat(member.getId()).isEqualTo(memberInfo.getId());
    }

    @Test
    void 회원_삭제하기() {
        Member member = new Member("wogns", "1234", "1234","wogns@naver.com");
        memberService.signUp(member);
        BDDMockito.doNothing().when(s3Uploader).deleteS3(Arrays.asList("1234"));
        memberService.deleteMember(member, "1234");
        assertThat(memberRepository.findAll().size()).isEqualTo(0);
    }

//    @Test
//    void 임시비밀번호발급() {
//        Member member = new Member("wogns", "1234", "1234","wogns@naver.com");
//        memberService.signUp(member);
//        sendEmailService.changePassword("4321", member.getEmail());
//        boolean matches = passwordEncoder.matches("4321", member.getPassword());
//        assertTrue(matches);
//    }

    @AfterEach
    void tearDown() {
        System.out.println("===================after each======================");
        memberRepository.deleteAll();
    }
}