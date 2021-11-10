package com.palette.controller;

import com.palette.domain.member.Member;
import com.palette.domain.post.Comment;
import com.palette.dto.request.CommentDto;
import com.palette.dto.response.CommentResponseDto;
import com.palette.dto.response.CommentResponsesDto;
import com.palette.service.CommentService;
import com.palette.service.PostService;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.palette.utils.constant.ConstantUtil.INIT_ID;
import static com.palette.utils.constant.HttpResponseUtil.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<CommentResponsesDto> getNextComment(@PathVariable Long postId, @RequestParam(value = "id", required = false) Long lastCommentId){
        List<CommentResponseDto> commentResponseDtos = commentService.findCommentByClickViewMore(postId, lastCommentId);
        return ResponseEntity.ok(CommentResponsesDto.builder().commentResponses(commentResponseDtos).build());
    }

    @GetMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentResponsesDto> getChildComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam(value = "id", required = false) Long lastCommentId){
        List<CommentResponseDto> childCommentResponseDtos = commentService.findChildComment(commentId, lastCommentId);
        return ResponseEntity.ok(CommentResponsesDto.builder().commentResponses(childCommentResponseDtos).build());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/{postId}/comment")
    public Long writeComment(@Login Member member, @PathVariable Long postId, @RequestBody CommentDto commentDto){
        Comment comment = Comment.builder().member(member).commentContent(commentDto.getContent()).build();
        Comment saveComment = commentService.writeComment(comment, postId, INIT_ID);
        return saveComment.getId();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/{postId}/comment/{commentId}")
    public Long writeChildComment(@Login Member member, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentDto){
        Comment comment = Comment.builder().member(member).commentContent(commentDto.getContent()).build();
        Comment saveComment = commentService.writeComment(comment, postId, commentId);
        return saveComment.getId();
    }

    @PutMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@Login Member member, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentDto){
        commentService.updateComment(member.getId(),commentId,commentDto.getContent());
        return RESPONSE_OK;
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@Login Member member, @PathVariable Long postId, @PathVariable Long commentId){
        commentService.deleteComment(member.getId(), commentId);
        return RESPONSE_OK;
    }
}
