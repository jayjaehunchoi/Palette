package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.dto.request.MemberUpdateDto;
import com.palette.exception.MemberException;
import com.palette.repository.MemberRepository;
import com.palette.utils.S3Uploader;
import lombok.RequiredArgsConstructor;;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    //회원가입시 비밀번호 암호화 후 저장
    @Transactional
    public Member signUp(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
        if(findMember != null) {
            throw new MemberException("중복된 ID입니다.");
        }
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    //로그인
    @Transactional(readOnly = true)
    public Member logIn(String email, String password) {
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException("아이디가 존재하지 않습니다."));
        checkPassword(password, findMember);
        return findMember;
    }

    //회원수정 중 이미지 변경
    @Transactional
    public void updateMember(Long id, MemberUpdateDto dto , String profileFileName) {
        Member member = memberRepository.findById(id).orElse(null); // 유저이메일로 유저찾음
        s3Uploader.deleteS3(Arrays.asList(member.getProfileFileName()));
        member.update(passwordEncoder.encode(dto.getPassword()),profileFileName);
    }

    @Transactional(readOnly = true)
    public Member getMemberInfo(Long id){
        return memberRepository.findById(id).orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));
    }

    public void deleteMember(Member member, String password){
        checkPassword(password,member);
        s3Uploader.deleteS3(Arrays.asList(member.getProfileFileName()));
        memberRepository.delete(member);
    }

    private void checkPassword(String password, Member findMember) {
        boolean matches = passwordEncoder.matches(password, findMember.getPassword());
        if(!matches) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }
    }

}