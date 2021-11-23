package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.dto.MailDto;
import com.palette.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    private MailDto createMail(String email, String name, String tempPassword){
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle(name+"님의 Palette 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Palette 임시비밀번호 안내 관련 이메일 입니다." + "[" + name + "]" +"님의 임시 비밀번호는 "
                + tempPassword + " 입니다.");
        return dto;
    }

    @Transactional
    public MailDto changePassword(String email, String name){
        String tempPassword = getTempPassword();
        MailDto dto = createMail(email, name, tempPassword);
        Member member = memberRepository.findByEmail(email).orElse(null);
        member.encodePassword(passwordEncoder.encode(tempPassword));

        return dto;
    }

    private String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailDto mailDto){
        log.info("이메일 전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        log.info("message.getFrom {}", message.getFrom());
        mailSender.send(message);
    }
}