package com.palette.utils.argumentresolver;

import com.palette.controller.auth.AuthorizationExtractor;
import com.palette.controller.auth.JwtTokenProvider;
import com.palette.domain.member.Member;
import com.palette.service.MemberService;
import com.palette.utils.constant.SessionUtil;
import com.palette.controller.auth.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("Login Argument Resolver 실행");
        String accessToken = AuthorizationExtractor.extract(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        if(jwtTokenProvider.isValidToken(accessToken)){
            String id = jwtTokenProvider.getPayload(accessToken);
            Member member = memberService.getMemberInfo(Long.valueOf(id));
            return member;
        }
        return null;
    }
}
