package com.palette.controller;

import com.palette.domain.member.Member;
import com.palette.domain.post.Comment;
import com.palette.dto.request.CommentDto;
import com.palette.dto.response.CommentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;

import static com.palette.controller.util.RestDocUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@AutoConfigureRestDocs
public class CommentControllerTest extends RestDocControllerTest{

    @Autowired CommentController commentController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = successRestDocsMockMvc(provider, commentController);
    }

    @Test
    void 게시물_댓글_조회_페이징() throws Exception {
        CommentResponseDto dto1 = new CommentResponseDto(1L, NAME, 1L, CONTENT, CREATED_DATE);
        CommentResponseDto dto2 = new CommentResponseDto(1L, NAME, 2L, CONTENT, CREATED_DATE);

        given(commentService.findCommentByClickViewMore(anyLong(),anyLong()))
                .willReturn(Arrays.asList(dto1,dto2));

        restDocsMockMvc.perform(get("/post/1/comment?id=0"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-get-paging",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                        ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));

    }

    @Test
    void 게시물_댓글_답글_조회_페이징() throws Exception{
        CommentResponseDto dto1 = new CommentResponseDto(1L, NAME, 1L, CONTENT, CREATED_DATE);
        CommentResponseDto dto2 = new CommentResponseDto(1L, NAME, 2L, CONTENT, CREATED_DATE);
        given(commentService.findChildComment(any(),any()))
                .willReturn(Arrays.asList(dto1,dto2));

        restDocsMockMvc.perform(get("/post/1/comment/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-get-child-paging",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));

    }

    @Test
    void 댓글_작성() throws Exception{
        CommentDto commentDto = new CommentDto(CONTENT);
        Comment comment = createComment(commentDto);
        given(commentService.writeComment(any(),any(),any())).willReturn(comment);

        restDocsMockMvc.perform(post("/post/1/comment")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON).header(AUTH, TOKEN))
                .andExpect(status().isCreated())
                .andDo(document("comment-create-comment",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 답글_작성() throws Exception{
        CommentDto commentDto = new CommentDto(CONTENT);
        Comment comment = createComment(commentDto);
        given(commentService.writeComment(any(),any(),any())).willReturn(comment);

        restDocsMockMvc.perform(post("/post/1/comment/1")
                .content(objectMapper.writeValueAsString(commentDto))
                .contentType(MediaType.APPLICATION_JSON).header(AUTH, TOKEN))
                .andExpect(status().isCreated())
                .andDo(document("comment-create-child-comment",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 댓글_업데이트() throws Exception{
        CommentDto commentDto = new CommentDto(CONTENT);
        Comment comment = createComment(commentDto);
        given(commentService.updateComment(any(),any(),any())).willReturn(comment);

        restDocsMockMvc.perform(put("/post/1/comment/1")
                .content(objectMapper.writeValueAsString(commentDto))
                .contentType(MediaType.APPLICATION_JSON).header(AUTH, TOKEN))
                .andExpect(status().isOk())
                .andDo(document("comment-update-comment",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 댓글_삭제() throws Exception{
        doNothing().when(commentService).deleteComment(1L,1L);

        restDocsMockMvc.perform(delete("/post/1/comment/1").header(AUTH, TOKEN))
                .andExpect(status().isOk())
                .andDo(document("comment-delete-comment",preprocessRequest(MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(MockMvcConfig.prettyPrintPreProcessor())));
    }


    private Comment createComment(CommentDto commentDto) {
        Comment comment = Comment.builder()
                .member(new Member(NAME, PASSWORD, IMAGE, EMAIL))
                .commentContent(commentDto.getContent())
                .build();
        return comment;
    }
}