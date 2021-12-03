package com.palette.controller.socialoauth.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KakaoProfile {
    private int id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @ToString
    @Getter
    public static class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @ToString
    @Getter
    public static class KakaoAccount {
        private Boolean profile_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
        private Boolean has_age_range;
        private Boolean age_range_needs_agreement;
        private Boolean has_birthday;
        private Boolean birthday_needs_agreement;
        private Boolean has_gender;
        private Boolean gender_needs_agreement;

        @ToString
        @Getter
        public static class Profile {
            private String nickname;
            private String thumbnail_image_url;
            private String profile_image_url;
        }
    }
}
