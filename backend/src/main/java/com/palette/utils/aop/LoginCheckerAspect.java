package com.palette.utils.aop;

import com.palette.domain.member.Member;
import com.palette.utils.constant.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static com.palette.utils.constant.SessionUtil.*;

@Component
@Slf4j
@Aspect
public class LoginCheckerAspect {

    @Before("@annotation(com.palette.utils.annotation.LoginChecker)")
    public void checkLogin(){
        log.info("[로그인 체커 작동]");
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        Member member = (Member) session.getAttribute(MEMBER);

        if(member == null){
            log.error("[Session 값이 없습니다.]");
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.") {};
        }
    }
}
