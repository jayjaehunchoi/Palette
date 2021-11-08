package com.palette.config;

import com.palette.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String upw;
    private String uname;
    private String profileFileName;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String upw, String uname, String profileFileName, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.upw = upw;
        this.uname = uname;
        this.profileFileName = profileFileName;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .uname((String) attributes.get("uname"))
                .email((String) attributes.get("email"))
                .profileFileName((String) attributes.get("profileFileName"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .uname((String) response.get("uname"))
                .email((String) response.get("email"))
                .profileFileName((String) response.get("profileFileName"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .uname(uname)
                .email(email)
                .profileFileName(profileFileName)
                .build();
    }
}