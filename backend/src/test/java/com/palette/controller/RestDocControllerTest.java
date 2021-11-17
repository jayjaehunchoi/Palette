package com.palette.controller;

import com.palette.domain.Period;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.domain.post.Post;
import com.palette.domain.post.PostGroup;
import com.palette.dto.request.PostRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static capital.scalable.restdocs.misc.AuthorizationSnippet.documentAuthorization;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class RestDocControllerTest extends ControllerTest{

    protected static final String NAME = "jaehunChoi";
    protected static final String PASSWORD = "qwer12345!";
    protected static final String EMAIL = "wogns0108@gmail.com";
    protected static final String IMAGE = "https://palette-bucket.s3.ap-northeast-2.amazonaws.com/static/BasicThumbnail.jpg";
    protected static final LocalDateTime CREATED_DATE= LocalDateTime.of(2021,11,10,10,0,0);
    protected static final LocalDate START = LocalDate.of(2021,11,10);
    protected static final LocalDate END = LocalDate.of(2021,11,12);
    protected static final String REGION = "서울";
    protected static final String TITLE = "제목";
    protected static final String CONTENT = "내용";
    protected static final String CODE = "das1-123s-fvj1-pqaz";
    protected static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM3MDgyNDgyLCJleHAiOjE2MzcwODYwODJ9.uIUqYuvBBTl34snJOJKTzsg4ULKdRg3A-sSJsCviQ0U";
    protected static final String AUTH = "Authorization";
    protected static final String GROUP_NAME = "groupName";
    protected static final String GROUP_INTRO = "This is Group";


    protected MockMvc restDocsMockMvc;

    protected Member createMember(){
        return new Member(NAME, PASSWORD,IMAGE,EMAIL);
    }

    protected Group createGroup() {
        return Group.builder().groupName(GROUP_NAME).groupsIntroduction(GROUP_INTRO).build();
    }
    protected PostGroup createPostGroup(){
        return new PostGroup(createMember(), TITLE, new Period(START,END),REGION);
    }
    protected Post createPost(){
        return Post.builder().title(TITLE)
                .content(CONTENT)
                .member(createMember())
                .region(REGION)
                .period(new Period(START,END))
                .build();
    }


    @BeforeEach
    void JwtSet(){
        given(jwtTokenProvider.isValidToken(any())).willReturn(true);
    }

}
