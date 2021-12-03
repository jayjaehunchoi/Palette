package com.palette.controller.socialoauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.controller.socialoauth.dto.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class KakaoAuthorization implements Authorization{
    private final RestTemplate restTemplate;


    @Value("${security.kakao.oauth.url}")
    private String callBackUrl;

    @Value("${security.kakao.client.secret}")
    private String clientSecret;

    @Value("${security.kakao.client.key}")
    private String clientKey;

    @Override
    public MultiValueMap<String, String> requestToken(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 고정값
        params.add("client_id", clientKey);
        params.add("redirect_uri", callBackUrl);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        return params;
    }

    @Override
    public HttpHeaders setTokenHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }

    @Override
    public HttpHeaders setProfileHeaders(OAuthToken oAuthToken){
        HttpHeaders headersForRequestProfile = new HttpHeaders();
        headersForRequestProfile.add("Authorization", "Bearer " + Objects.requireNonNull(oAuthToken).getAccess_token());
        headersForRequestProfile.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headersForRequestProfile;
    }


    @Override
    public ResponseEntity<String> createRequest(String url, HttpEntity<?> request) {
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
    }
}
