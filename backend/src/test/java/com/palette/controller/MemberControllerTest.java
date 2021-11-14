package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.dto.request.MemberDto;
import com.palette.dto.request.MemberUpdateDto;
import com.palette.dto.response.MemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static com.palette.utils.constant.SessionUtil.MEMBER;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartBody;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
public class MemberControllerTest extends RestDocControllerTest{

    @Autowired MemberController memberController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider,memberController);
    }

    @Test
    void 회원가입() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        MemberDto dto = MemberDto.builder().name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        String memberJson = objectMapper.writeValueAsString(dto);
        MockMultipartFile json = new MockMultipartFile("member-data", "json", "application/json",memberJson.getBytes(StandardCharsets.UTF_8));

        given(s3Uploader.uploadSingleFile(any())).willReturn(new MyFile("imagefile.jpeg",IMAGE));
        given(memberService.signUp(any())).willReturn(new Member(dto.getName(), dto.getPassword(),IMAGE,dto.getEmail()));

        restDocsMockMvc.perform(multipart("/signup")
                .file(json)
                .file(file)
                .content("multipart/mixed")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")).andDo(print()).andExpect(status().isCreated())
                .andDo(document("member-signup"
                        ,requestParts(partWithName("member-data").description("MemberDto")
                                , partWithName("file").description("multipart file"))
                        ,requestPartBody("member-data")));

    }

    @Test
    void 로그인() throws Exception {
        MemberDto dto = MemberDto.builder().name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        String memberJson = objectMapper.writeValueAsString(dto);
        given(memberService.logIn(any(), any())).willReturn(new Member(dto.getName(), dto.getPassword(),IMAGE,dto.getEmail()));

        restDocsMockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson)
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-login"));
    }

    @Test
    void 로그아웃() throws Exception{
        session.setAttribute(MEMBER, createMember());

        restDocsMockMvc.perform(get("/signout").session(session))
                .andExpect(status().isOk())
                .andDo(document("member-logout"));
    }

    @Test
    void 멤버정보_가져오기() throws Exception{
        session.setAttribute(MEMBER, createMember());
        Member member = createMember();
        given(memberService.getMemberInfo(anyLong())).willReturn(member);
        restDocsMockMvc.perform(get("/member/1").session(session)).andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(new MemberResponseDto(member))))
                .andExpect(status().isOk())
                .andDo(document("member-getMember",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 멤버정보_수정() throws Exception{
        session.setAttribute(MEMBER, createMember());
        MockMultipartFile file = new MockMultipartFile("file", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        MemberUpdateDto dto = new MemberUpdateDto(PASSWORD);
        String memberJson = objectMapper.writeValueAsString(dto);
        MockMultipartFile json = new MockMultipartFile("member-update-data", "json", "application/json",memberJson.getBytes(StandardCharsets.UTF_8));
        given(s3Uploader.uploadSingleFile(any())).willReturn(new MyFile(IMAGE,IMAGE));
        doNothing().when(memberService).updateMember(any(),any(),any());
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/member/1");
        builder.with(request -> { request.setMethod("PUT"); return request; });

        restDocsMockMvc.perform(builder
                .file(json)
                .file(file)
                        .content("multipart/mixed")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8").session(session)).andDo(print()).andExpect(status().isOk())
                .andDo(document("member-update"
                        ,requestParts(partWithName("member-update-data").description("memberUpdateDto")
                                , partWithName("file").description("multipart file"))
                        ,requestPartBody("member-update-data")));

    }

    @Test
    void 멤버_삭제() throws Exception{
        session.setAttribute(MEMBER, createMember());
        doNothing().when(memberService).deleteMember(any(),any());
        MemberUpdateDto dto = new MemberUpdateDto(PASSWORD);
        String json = objectMapper.writeValueAsString(dto);

        restDocsMockMvc.perform(delete("/member")
                .content(json)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON).session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-delete"));

    }

    private Member createMember(){
        return new Member(NAME, PASSWORD,IMAGE,EMAIL);
    }


}
