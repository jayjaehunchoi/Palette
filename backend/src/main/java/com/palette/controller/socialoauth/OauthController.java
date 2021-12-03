package com.palette.controller.socialoauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.controller.auth.JwtTokenProvider;
import com.palette.controller.auth.Token;
import com.palette.controller.socialoauth.dto.KakaoProfile;
import com.palette.controller.socialoauth.dto.OAuthToken;
import com.palette.domain.member.Member;
import com.palette.dto.request.MemberDto;
import com.palette.dto.response.MemberResponseDto;
import com.palette.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OauthController {
    private final Authorization authorization;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    private String tokenUrl = "https://kauth.kakao.com/oauth/token";
    private String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

    @GetMapping("/kakao/callback")
    public KakaoProfile getCallBack(String code){
        HttpEntity<MultiValueMap<String, String>> tokenHttpEntity =
                new HttpEntity<>(authorization.requestToken(code), authorization.setTokenHeaders());

        ResponseEntity<String> tokenResponse = authorization.createRequest(tokenUrl, tokenHttpEntity);

        OAuthToken oAuthToken = null;
        try{
            oAuthToken = objectMapper.readValue(tokenResponse.getBody(), OAuthToken.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        HttpHeaders profileHeaders = authorization.setProfileHeaders(oAuthToken);
        HttpEntity<Object> profileHttpEntity = new HttpEntity<>(profileHeaders);
        ResponseEntity<String> profileResponse = authorization.createRequest(userInfoUrl, profileHttpEntity);

        KakaoProfile profile = null;
        try {
            profile = objectMapper.readValue(profileResponse.getBody(), KakaoProfile.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        log.info("카카오 프로필 정보 = {}", profile);

        return profile;
    }

    @PostMapping("/auth")
    public ResponseEntity<MemberResponseDto> socialLogin(@RequestBody KakaoProfile profile, HttpServletResponse response){
        Member findMember = memberService.findByEmail(profile.getKakao_account().getEmail());
        if(findMember == null){
            log.info("회원 가입 실행 {}", findMember);
            Member newMember = Member.builder()
                    .name(profile.getProperties().getNickname())
                    .password(UUID.randomUUID().toString().substring(0,12))
                    .email(profile.getKakao_account().getEmail())
                    .profileFileName(profile.getKakao_account().getProfile().getProfile_image_url())
                    .build();

            findMember = memberService.signUp(newMember);
        }

        log.info("로그인 실행 {}", findMember);
        Token accessToken = jwtTokenProvider.createAccessToken(String.valueOf(findMember.getId()));
        MemberResponseDto memberResponseDto = new MemberResponseDto(findMember);
        memberResponseDto.setAccessToken(accessToken);
        response.setHeader("Authorization", accessToken.getValue());
        return ResponseEntity.ok(memberResponseDto);
    }
}
