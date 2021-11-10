package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.Post;
import com.palette.domain.post.PostGroup;
import com.palette.dto.GeneralResponse;
import com.palette.dto.request.PostRequestDto;
import com.palette.dto.response.*;
import com.palette.utils.constant.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@AutoConfigureRestDocs
public class PostControllerTest extends RestDocControllerTest{

    @Autowired
    PostController postController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, postController);

        Member member = createMember();
        session.setAttribute(SessionUtil.MEMBER,member);
    }

    @Test
    void 그룹거치지않고_게시물_조회() throws Exception {
        StoryListResponseDto dto = new StoryListResponseDto(1L, NAME, 1L, TITLE, 100);

        given(postService.findStoryList(any(),anyInt())).willReturn(Arrays.asList(dto));
        restDocsMockMvc.perform(get("/post?name=jaehunChoi&region=서울&title=제목"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new StoryListResponsesDto(Arrays.asList(dto)))))
                .andDo(document("post-get-with-filter"));

    }

    @Test
    void 단건_게시물_조회() throws Exception{
        PostResponseDto postResponseDto = new PostResponseDto(new Post(TITLE, CONTENT, new Member(NAME, PASSWORD, IMAGE, EMAIL), new Period(START, END), REGION));
        given(postService.findSinglePost(any(),any())).willReturn(postResponseDto);
        restDocsMockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(postResponseDto)))
                .andDo(print())
                .andDo(document("post-get-single-post"));
    }

    @Test
    void 좋아요_조회() throws Exception{
        given(likeService.findLikeMemberByPost(any(),any()))
                .willReturn(Arrays.asList(new Member(NAME,PASSWORD,IMAGE,EMAIL)));

        LikeResponseDto likeResponseDto = new LikeResponseDto(new Member(NAME, PASSWORD, IMAGE, EMAIL));
        restDocsMockMvc.perform(get("/post/1/like?likeId=0"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(new LikeResponsesDto(Arrays.asList(likeResponseDto)))))
                .andDo(print())
                .andDo(document("post-get-like-paging"));
    }

    @Test
    void 좋아요_클릭() throws Exception{
        given(likeService.pushLike(any(),any())).willReturn(1);
        GeneralResponse.builder().data(1).build();
        restDocsMockMvc.perform(post("/post/1/like"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-push-like"));
    }

    @Test
    void 게시물_업로드() throws Exception{
        Member member = createMember();
        PostGroup postGroup = createPostGroup(member);
        PostRequestDto postRequestDto = new PostRequestDto(TITLE, CONTENT);

        MockMultipartFile files = new MockMultipartFile("files", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        MockMultipartFile files2 = new MockMultipartFile("files", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());

        String pg = objectMapper.writeValueAsString(postRequestDto);
        MockMultipartFile json = new MockMultipartFile("data", "json", "application/json", pg.getBytes(StandardCharsets.UTF_8));

        given(postGroupService.findById(any())).willReturn(postGroup);
        doNothing().when(postService).isAvailablePostOnPostGroup(postGroup,1L);
        MyFile myFile = new MyFile("image", "image");
        given(s3Uploader.uploadFiles(any())).willReturn(Arrays.asList(myFile));

        Post post = createPost(postRequestDto,member,postGroup);
        given(postService.write(any(),any(),any())).willReturn(post);

        restDocsMockMvc.perform(multipart("/postgroup/1/post")
                .file(files)
                .file(files2)
                .file(json)
                .content("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")).andDo(print()).andExpect(status().isCreated())
                .andDo(document("post-create-post"));
    }

    @Test
    void 게시물_업데이트() throws Exception{
        Member member = createMember();
        PostRequestDto postRequestDto = new PostRequestDto(TITLE, CONTENT);
        validate(postRequestDto);


        doNothing().when(postService).update(1L,postRequestDto);

        String content = objectMapper.writeValueAsString(postRequestDto);
        restDocsMockMvc.perform(put("/postgroup/1/post/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("post-update-post"));
    }

    @Test
    void 게시물_삭제() throws Exception {
        PostRequestDto postRequestDto = new PostRequestDto(TITLE, CONTENT);
        validate(postRequestDto);

        doNothing().when(postService).delete(1L);

        restDocsMockMvc.perform(delete("/postgroup/1/post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-delete-post"));
    }

    private void validate(PostRequestDto postRequestDto){
        Member member = createMember();
        PostGroup postGroup = createPostGroup(member);
        Post post = createPost(postRequestDto,member, postGroup);
        given(postService.findById(any())).willReturn(post);
        given(postGroupService.findById(any())).willReturn(postGroup);
        doNothing().when(postService).isAvailablePostOnPostGroup(postGroup,1L);
        doNothing().when(postService).isAvailableUpdatePost(post,member);
    }

    private Member createMember(){
        return new Member(NAME,PASSWORD,IMAGE,EMAIL);
    }

    private PostGroup createPostGroup(Member member){
        return new PostGroup(member, TITLE, new Period(START,END),REGION);
    }

    private Post createPost(PostRequestDto postRequestDto, Member member, PostGroup postGroup){
        return Post.builder().title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .member(member)
                .region(postGroup.getRegion())
                .period(postGroup.getPeriod())
                .build();
    }

}
