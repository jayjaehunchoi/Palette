package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
public class PageControllerTest extends RestDocControllerTest{

    @Autowired PageController pageController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, pageController);
    }

    @Test
    void 게시물_그룹_총_페이지() throws Exception {
        given(postGroupService.getTotalPage(any())).willReturn(10L);
        restDocsMockMvc.perform(get("/page/postgroup?name=jaehunChoi"))
                .andExpect(status().isOk())
                .andDo(document("page-postgroup-page",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 게시물_총_페이지() throws Exception{
        given(postService.getTotalPage(any())).willReturn(10L);
        restDocsMockMvc.perform(get("/page/post?region=서울"))
                .andExpect(status().isOk())
                .andDo(document("page-post-page",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));
    }
}
