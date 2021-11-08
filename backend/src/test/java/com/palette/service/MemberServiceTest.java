package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.repository.MemberRepository;
import com.palette.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() {
        Member member = new Member("wogns", "1234", "wogns","123");
        memberService.signUpUser(member);

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);
        boolean matches = passwordEncoder.matches("1234", members.get(0).getPassword());
        assertTrue(matches);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }
}