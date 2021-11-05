package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpUser(Member member) {
        member.encodePassword(passwordEncoder.encode(member.getUpw()));
        memberRepository.save(member);
    }
}