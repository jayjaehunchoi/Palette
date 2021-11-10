package com.palette.controller;

import com.palette.RestDocUtil;
import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.PostGroup;
import com.palette.dto.PeriodDto;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.dto.response.PostGroupsResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.utils.constant.ConstantUtil;
import com.palette.utils.constant.SessionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureRestDocs
public class PostGroupControllerTest extends RestDocControllerTest{

    @Autowired PostGroupController postGroupController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, postGroupController);

        Member member = new Member(NAME,PASSWORD,IMAGE,EMAIL);
        session.setAttribute(SessionUtil.MEMBER,member);
    }

    @Test
    void 필터_걸고_포스트_그룹_가져오기() throws Exception {
        PostGroupResponseDto dto1 = new PostGroupResponseDto(1L, 1L, NAME, "title", IMAGE, START, END, REGION);
        PostGroupResponseDto dto2 = new PostGroupResponseDto(1L, 1L, NAME, "title", IMAGE, START, END, REGION);

        List<PostGroupResponseDto> responses = Arrays.asList(dto1, dto2);
        PostGroupsResponseDto dtos = PostGroupsResponseDto.builder().postGroupResponses(responses).build();

        given(postGroupService.findPostGroupByMember(anyString(),anyInt())).willReturn(responses);

        ResultActions result = this.restDocsMockMvc.perform(get("/postgroup?filter=member&condition=jaehunChoi")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk()).andDo(print()).andDo(document("postgroup-get-postgroup"));

    }

    @Test
    void 포스트_그룹내_포스트_보기() throws Exception {
        StoryListResponseDto dto1 = new StoryListResponseDto(1L, NAME, 1L, TITLE, 10);
        StoryListResponseDto dto2 = new StoryListResponseDto(1L, NAME, 2L, TITLE, 50);

        List<StoryListResponseDto> dtos = Arrays.asList(dto1, dto2);

        given(postService.findStoryList(any(), anyInt())).willReturn(dtos);

        restDocsMockMvc.perform(get("/postgroup/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("postgroup-get-post"));
    }

    @Test
    void 포스트_그룹_생성() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        PostGroupDto data = new PostGroupDto(TITLE, new PeriodDto(START, END), REGION);
        PostGroup postGroup = PostGroup.builder().title(data.getTitle())
                .period(new Period(data.getPeriod()))
                .region(data.getRegion())
                .build();
        MyFile myFile = new MyFile("imagefile.jpeg", "imagefile.jpeg");
        postGroup.setThumbNail(myFile);

        String pg = objectMapper.writeValueAsString(data);
        MockMultipartFile json = new MockMultipartFile("data", "json", "application/json", pg.getBytes(StandardCharsets.UTF_8));
        given(postGroupService.createPostGroup(any())).willReturn(postGroup);
        given(s3Uploader.uploadFiles(any())).willReturn(Arrays.asList(myFile));

        restDocsMockMvc.perform(multipart("/postgroup")
                .file(json)
                .file(file)
                .content("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")).andDo(print()).andExpect(status().isCreated())
                .andDo(document("postgroup-create-group"));


    }

    @Test
    void 포스트_그룹_업데이트() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        PostGroupDto data = new PostGroupDto(TITLE, new PeriodDto(START, END), REGION);
        PostGroup postGroup = PostGroup.builder().title(data.getTitle())
                .period(new Period(data.getPeriod()))
                .region(data.getRegion())
                .build();
        MyFile myFile = new MyFile("imagefile.jpeg", "imagefile.jpeg");
        postGroup.setThumbNail(myFile);

        String pg = objectMapper.writeValueAsString(data);
        MockMultipartFile json = new MockMultipartFile("data", "json", "application/json", pg.getBytes(StandardCharsets.UTF_8));
        doNothing().when(postGroupService).updatePostGroup(anyLong(),any(),any());
        given(postGroupService.checkMemberAuth(any(),anyLong())).willReturn(postGroup);
        given(s3Uploader.uploadFiles(any())).willReturn(Arrays.asList(myFile));
        doNothing().when(s3Uploader).deleteS3(any());

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/postgroup/1");
        builder.with(request -> { request.setMethod("PUT"); return request; });

        restDocsMockMvc.perform(builder
                .file(json)
                .file(file)
                .content("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")).andDo(print()).andExpect(status().isOk())
                .andDo(document("postgroup-update-group"));
    }

    @Test
    void 포스트_그룹_삭제() throws Exception{
        PostGroup postGroup = PostGroup.builder().title(TITLE)
                .period(new Period(START,END))
                .region(REGION)
                .build();

        given(postGroupService.checkMemberAuth(any(),anyLong())).willReturn(postGroup);
        given(postService.findPostIdsByPostGroupId(anyLong())).willReturn(Arrays.asList(1L));
        doNothing().when(postGroupService).deletePostGroup(postGroup);

        restDocsMockMvc.perform(delete("/postgroup/1")).andExpect(status().isOk())
                .andDo(document("postgroup-delete-group"));

    }

    @AfterEach
    void tearDown(){
        session.clearAttributes();;
        session = null;
    }


}
