package com.palette.controller.socialoauth;

import com.palette.controller.socialoauth.dto.OAuthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface Authorization {

    MultiValueMap<String, String> requestToken(String code);
    HttpHeaders setTokenHeaders();
    public HttpHeaders setProfileHeaders(OAuthToken oAuthToken);
    ResponseEntity<String> createRequest(String url, HttpEntity<?> request);

}
