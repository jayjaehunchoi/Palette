package com.palette.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.controller.auth.JwtTokenProvider;
import com.palette.service.MemberService;
import com.palette.utils.HtmlCharacterEscapes;
import com.palette.utils.argumentresolver.LoginArgumentResolver;
import com.palette.utils.constant.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.palette.utils.constant.ConstantUtil.*;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(jwtTokenProvider,memberService));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5500","http://127.0.0.1:5500","http://www.palette-travel.com", "https://www.palette-travel.com") // todo : 프론트 서버 배포 후 변경
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Accept", "Content-Type",
                        "Origin", "Access-Control-Allow-Credentials", "Set-Cookie", "Access-Control-Allow-Headers",
                        "Access-Control-Allow-Methods", "Access-Control-Allow-Origin")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
