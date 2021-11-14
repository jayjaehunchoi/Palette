package com.palette.controller;

import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.dto.MailDto;
import com.palette.dto.request.EmailDto;
import com.palette.dto.request.MemberDto;
import com.palette.dto.request.MemberUpdateDto;
import com.palette.dto.response.MemberResponseDto;
import com.palette.service.MemberService;
import com.palette.service.SendEmailService;
import com.palette.utils.S3Uploader;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.IOException;

import static com.palette.utils.constant.SessionUtil.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final HttpSession session;
    private final S3Uploader s3Uploader;
    private final SendEmailService sendEmailService;

    // 회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Long signUp(@RequestPart("member-data") @Valid MemberDto memberDto, @RequestPart("file")MultipartFile multipartFile) throws IOException {
        MyFile myFile = s3Uploader.uploadSingleFile(multipartFile);
        Member member = Member.builder()
                .name(memberDto.getName())
                .password((memberDto.getPassword()))
                .email(memberDto.getEmail())
                .profileFileName(myFile.getStoreFileName())
                .build();

        Member saveMember = memberService.signUp(member);
        return member.getId();
    }

    //로그인
    @PostMapping("/signin")
    public void logIn(@RequestBody MemberDto memberDto) {
        Member findMember = memberService.logIn(memberDto.getEmail(), memberDto.getPassword());
        session.setAttribute(MEMBER, findMember);
        log.info("session {}",session.getAttribute(MEMBER));
    }

    //로그아웃
    @GetMapping("/signout")
    public void logout() {
        session.removeAttribute(MEMBER);
        log.info("로그아웃 실행");
    }

    @GetMapping("/member/{memberId}")
    public MemberResponseDto getMember(@PathVariable Long memberId){
        Member findMember = memberService.getMemberInfo(memberId);
        return new MemberResponseDto(findMember);
    }

    @PutMapping("/member/{memberId}")
    public void updateMember(@PathVariable Long memberId,@RequestPart("member-update-data") @Valid MemberUpdateDto dto, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        MyFile myFile = s3Uploader.uploadSingleFile(multipartFile);
        memberService.updateMember(memberId, dto, myFile.getStoreFileName());
    }

    @DeleteMapping("/member")
    public void deleteMember(@Login Member member, @RequestBody MemberUpdateDto dto){
       memberService.deleteMember(member,dto.getPassword());
    }

//    //Email과 name의 일치여부를 check하는 컨트롤러
//    @GetMapping("/checkEmail")
//    public Map<String, Boolean> pw_find(@RequestBody @Valid EmailDto emailDto) {
//        Map<String,Boolean> json = new HashMap<>();
//        boolean pwFindCheck = memberService.checkEmail(emailDto.getEmail(), emailDto.getName());
//        System.out.println(pwFindCheck);
//        json.put("check", pwFindCheck);
//        return json;
//    }

    //등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 pw를 변경하는 컨트롤러
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody @Valid EmailDto emailDto){
        MailDto dto = sendEmailService.changePassword(emailDto.getEmail(), emailDto.getName());
        sendEmailService.mailSend(dto);
    }
}
