package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.exception.MemberException;
import com.palette.repository.MemberRepository;
import lombok.RequiredArgsConstructor;;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

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
    public Member logIn(String email, String password) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException("아이디가 존재하지 않습니다."));
        boolean matches = passwordEncoder.matches(password, findMember.getPassword());
        if(!matches) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }
        return findMember;
    }

    //회원수정 중 이미지 변경
    //todo
    @Transactional
    public void imgUpdate(String email, String profileFileName) {
        Member member = memberRepository.findByEmail(email).orElseThrow(); // 유저이메일로 유저찾음
        member.changeProfileFile(profileFileName);
    }

}