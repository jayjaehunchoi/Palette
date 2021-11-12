package com.palette.controller;

import com.palette.domain.member.Member;
import com.palette.dto.MemberDto;
import com.palette.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.palette.utils.constant.SessionUtil.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final HttpSession session;

    // 회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Long signUp(@RequestBody @Valid MemberDto memberDto) {
        Member member = Member.builder()
                .name(memberDto.getName())
                .password((memberDto.getPassword()))
                .email(memberDto.getEmail())
                .build();

        Member saveMember = memberService.signUp(member);
        return member.getId();
    }

    //로그인
    @PostMapping("/signin")
    public void logIn(@RequestBody MemberDto memberDto) {
        Member member = Member.builder()
                .name(memberDto.getName())
                .password((memberDto.getPassword()))
                .email(memberDto.getEmail())
                .build();

        Member findMember = memberService.logIn(member.getEmail(), member.getPassword());
        session.setAttribute(MEMBER, findMember);
        log.info("session {}",session.getAttribute(MEMBER));
    }

    //로그아웃
    @PostMapping("/signout")
    public void logout() {
        session.removeAttribute(MEMBER);
        log.info("로그아웃 실행");
    }

}
