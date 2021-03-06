package com.palette.controller;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.Post;
import com.palette.domain.post.PostGroup;
import com.palette.dto.GeneralResponse;
import com.palette.dto.request.PostRequestDto;
import com.palette.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.palette.controller.util.RestDocUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@AutoConfigureRestDocs
public class PostControllerTest extends RestDocControllerTest{

    @Autowired
    PostController postController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = successRestDocsMockMvc(provider, postController);
    }

    @Test
    void 그룹거치지않고_게시물_조회() throws Exception {
        StoryListResponseDto dto = new StoryListResponseDto(1L, NAME, 1L, TITLE, 100);

        given(postService.findStoryList(any(),anyInt())).willReturn(Arrays.asList(dto));
        restDocsMockMvc.perform(get("/api/post?name=jaehunChoi&region=서울&title=제목"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new StoryListResponsesDto(Arrays.asList(dto)))))
                .andDo(document("post-get-with-filter",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));

    }

    @Test
    void 단건_게시물_조회() throws Exception{
        PostResponseDto postResponseDto = new PostResponseDto(new Post(TITLE, CONTENT, new Member(NAME, PASSWORD, IMAGE, EMAIL), new Period(START, END), REGION));
        postResponseDto.setImages(Arrays.asList(IMAGE, IMAGE));
        CommentResponseDto dto1 = new CommentResponseDto(1L, NAME, 1L, CONTENT, CREATED_DATE);
        CommentResponseDto dto2 = new CommentResponseDto(1L, NAME, 2L, CONTENT, CREATED_DATE);
        postResponseDto.setComments(Arrays.asList(dto1, dto2));
        given(postService.findSinglePost(any(),any())).willReturn(postResponseDto);
        restDocsMockMvc.perform(get("/api/post/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(postResponseDto)))
                .andDo(print())
                .andDo(document("post-get-single-post",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 좋아요_조회() throws Exception{
        given(likeService.findLikeMemberByPost(any(),any()))
                .willReturn(Arrays.asList(new Member(NAME,PASSWORD,IMAGE,EMAIL)));

        LikeResponseDto likeResponseDto = new LikeResponseDto(new Member(NAME, PASSWORD, IMAGE, EMAIL));
        restDocsMockMvc.perform(get("/api/post/1/like?likeId=0"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(new LikeResponsesDto(Arrays.asList(likeResponseDto)))))
                .andDo(print())
                .andDo(document("post-get-like-paging",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 좋아요_클릭() throws Exception{
        given(likeService.pushLike(any(),any())).willReturn(1);
        GeneralResponse.builder().data(1).build();
        restDocsMockMvc.perform(post("/api/post/1/like").header(AUTH, TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-push-like",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 게시물_업로드() throws Exception{
        Member member = createMember();
        PostGroup postGroup = createPostGroup();
        PostRequestDto postRequestDto = new PostRequestDto(TITLE, CONTENT);

        MockMultipartFile files = new MockMultipartFile("files", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        MockMultipartFile files2 = new MockMultipartFile("files", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());

        String pg = objectMapper.writeValueAsString(postRequestDto);
        MockMultipartFile json = new MockMultipartFile("data", "json", "application/json", pg.getBytes(StandardCharsets.UTF_8));

        given(postGroupService.findById(any())).willReturn(postGroup);
        doNothing().when(postService).isAvailablePostOnPostGroup(postGroup,1L);
        MyFile myFile = new MyFile("image", "image");
        given(s3Uploader.uploadFiles(any())).willReturn(Arrays.asList(myFile));

        Post post = createPost();
        given(postService.write(any(),any(),any())).willReturn(post);

        restDocsMockMvc.perform(multipart("/api/postgroup/1/post")
                .file(files)
                .file(files2)
                .file(json)
                .content("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8").header(AUTH, TOKEN)).andDo(print()).andExpect(status().isCreated())
                .andDo(document("post-create-post"
                        ,requestParts(partWithName("data").description("postRequestDto")
                        , partWithName("files").description("multiple files"))
                        ,requestPartBody("data")));
    }

    @Test
    void 게시물_업데이트() throws Exception{
        PostRequestDto postRequestDto = new PostRequestDto(TITLE, CONTENT);
        validate();

        doNothing().when(postService).update(1L,postRequestDto);

        String content = objectMapper.writeValueAsString(postRequestDto);
        restDocsMockMvc.perform(put("/api/postgroup/1/post/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON).header(AUTH, TOKEN))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("post-update-post"
                        ,preprocessRequest(MockMvcConfig.prettyPrintPreProcessor())
                        ,preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 게시물_삭제() throws Exception {
        validate();
        doNothing().when(postService).delete(1L);

        restDocsMockMvc.perform(delete("/api/postgroup/1/post/1").header(AUTH, TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-delete-post",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    private void validate(){
        Member member = createMember();
        PostGroup postGroup = createPostGroup();
        Post post = createPost();
        given(postService.findById(any())).willReturn(post);
        given(postGroupService.findById(any())).willReturn(postGroup);
        doNothing().when(postService).isAvailablePostOnPostGroup(postGroup,1L);
        doNothing().when(postService).isAvailableUpdatePost(post,member);
    }

}
