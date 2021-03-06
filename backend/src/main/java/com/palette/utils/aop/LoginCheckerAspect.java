package com.palette.utils.aop;

import com.palette.controller.auth.AuthorizationExtractor;
import com.palette.controller.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
@Slf4j
@Aspect
public class LoginCheckerAspect {

    private final JwtTokenProvider jwtTokenProvider;

    @Before("@annotation(com.palette.utils.annotation.LoginChecker)")
    public void checkLogin(){
        log.info("[로그인 체커 작동]");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        validateAccessToken(request);
    }

    private void validateAccessToken(HttpServletRequest request){
        String accessToken = AuthorizationExtractor.extract(request);
        log.info("[로그인 체커] 접근한 토큰 값 = {}",accessToken);
        if(accessToken == null){
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.") {};
        }
        boolean validToken = jwtTokenProvider.isValidToken(accessToken);
        if(!validToken){
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.") {};
        }
    }
}
