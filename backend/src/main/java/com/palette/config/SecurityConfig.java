package com.palette.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 설정들을 활성화시킨다.
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                //h2-console 화면을 사용하기 위해 해당 옵션들을 disable한다.
                .and()
                .authorizeRequests()
                //URL별 권한 관리를 설정하는 옵션의 시작점.
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile", "/**").permitAll() //권한 관리 대상들을 지정
                //.antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated() //설정된 이외의 나머지 URL 값들을 의미한다.
                .and()
                .logout()
                .logoutSuccessUrl("/")
                //로그아웃 기능에 대한 여러 설정의 진입점.
                //로그아웃 성공 시 / 주소로 이동한다.
                .and()
                .oauth2Login()
                //OAuth2 로그인 기능에 대한 여러 설정의 진입점.
                .userInfoEndpoint()
                //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당한다.
                .userService(customOAuth2UserService);
                //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다.
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
