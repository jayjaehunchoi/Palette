package com.palette.controller;

import com.palette.domain.member.Member;
import com.palette.domain.post.Comment;
import com.palette.dto.MemberDto;
import com.palette.dto.request.CommentDto;
import com.palette.service.MemberService;
import com.palette.utils.SessionUtil;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.palette.utils.ConstantUtil.INIT_ID;
import static com.palette.utils.SessionUtil.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final HttpSession session;

    // 회원가입
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long signUp(@RequestBody MemberDto memberDto) {
        Member member = Member.builder()
                .name(memberDto.getName())
                .password((memberDto.getPassword()))
                .email(memberDto.getEmail())
                .build();

        Member saveMember = memberService.signUp(member);
        return member.getId();
    }

    //로그인
    @PostMapping("/login")
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
    @GetMapping("/logout")
    public void logOut() {
        session.removeAttribute(MEMBER);
    }
}
