package com.palette.controller;

import com.palette.controller.auth.JwtTokenProvider;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.dto.MailDto;
import com.palette.controller.auth.Token;
import com.palette.dto.request.EmailDto;
import com.palette.dto.request.MemberDto;
import com.palette.dto.request.MemberUpdateDto;
import com.palette.dto.response.MemberResponseDto;
import com.palette.service.MemberService;
import com.palette.service.SendEmailService;
import com.palette.utils.S3Uploader;
import com.palette.controller.auth.AuthenticationPrincipal;
import com.palette.utils.annotation.LoginChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final S3Uploader s3Uploader;
    private final SendEmailService sendEmailService;
    private final JwtTokenProvider jwtTokenProvider;

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

        memberService.signUp(member);
        return member.getId();
    }

    @PostMapping("/signin")
    public ResponseEntity<MemberResponseDto> logIn(@RequestBody MemberDto memberDto, HttpServletResponse response) {
        Member findMember = memberService.logIn(memberDto.getEmail(), memberDto.getPassword());
        Token accessToken = jwtTokenProvider.createAccessToken(String.valueOf(findMember.getId()));
        MemberResponseDto memberResponseDto = new MemberResponseDto(findMember);
        memberResponseDto.setAccessToken(accessToken);
        response.setHeader("Authorization", accessToken.getValue());
        return ResponseEntity.ok(memberResponseDto);
    }

    @LoginChecker
    @GetMapping("/member")
    public MemberResponseDto getMember(@AuthenticationPrincipal Member member){
        Member findMember = memberService.getMemberInfo(member.getId());
        return new MemberResponseDto(findMember);
    }

    @LoginChecker
    @PutMapping("/member")
    public void updateMember(@AuthenticationPrincipal Member member, @RequestPart("member-update-data") @Valid MemberUpdateDto dto, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        MyFile myFile = s3Uploader.uploadSingleFile(multipartFile);
        memberService.updateMember(member.getId(), dto, myFile.getStoreFileName());
    }

    @LoginChecker
    @DeleteMapping("/member")
    public void deleteMember(@AuthenticationPrincipal Member member, @RequestBody MemberUpdateDto dto){
       memberService.deleteMember(member,dto.getPassword());
    }

    //등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 pw를 변경하는 컨트롤러
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody @Valid EmailDto emailDto){
        MailDto dto = sendEmailService.changePassword(emailDto.getEmail(), emailDto.getName());
        sendEmailService.mailSend(dto);
    }
}
