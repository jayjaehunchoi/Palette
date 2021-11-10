package com.palette.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class RestDocControllerTest extends ControllerTest{

    protected static final String NAME = "jaehunChoi";
    protected static final String PASSWORD = "1234";
    protected static final String EMAIL = "wogns0108@gmail.com";
    protected static final String IMAGE = "https://palette-bucket.s3.ap-northeast-2.amazonaws.com/static/BasicThumbnail.jpg";
    protected static final LocalDateTime START = LocalDateTime.of(2021,11,10,10,0,0);
    protected static final LocalDateTime END = LocalDateTime.of(2021,11,12,10,0,0);
    protected static final String REGION = "서울";
    protected static final String TITLE = "제목";
    protected static final String CONTENT = "내용";


    protected MockMvc restDocsMockMvc;
    protected MockHttpSession session = new MockHttpSession();


}
