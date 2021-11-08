package com.palette.service;

import com.palette.domain.post.Comment;
import com.palette.domain.post.Post;
import com.palette.dto.response.CommentResponseDto;
import com.palette.exception.CommentException;
import com.palette.exception.PostException;
import com.palette.repository.CommentRepository;
import com.palette.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment writeComment(Comment comment, Long postId, Long commentId){
        Post findPost = postRepository.findById(postId).orElse(null);
        isPostExist(findPost);
        comment.writeComment(findPost, commentId);
        return comment;
    }

    public Comment updateComment(Long memberId, Long commentId, String updateContent){
        Comment findComment = commentRepository.findById(commentId).orElse(null);
        isMemberHaveAuthToUpdate(findComment, memberId);
        return findComment.updateComment(updateContent);
    }

    public void deleteComment(Long memberId, Long commentId){
        Comment findComment = commentRepository.findById(commentId).orElse(null);
        isMemberHaveAuthToUpdate(findComment, memberId);
        findComment.removeComment(findComment);
        commentRepository.delete(findComment);
        commentRepository.deleteAllChildById(commentId);
    }

    /**
     * @param commentId - 현재 답글이 보고 싶은 comment의 id
     * @param curCommentId - 현재 답글의 commentId
     * @return 댓글 dto return
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findChildComment(Long commentId, Long curCommentId){
        return commentRepository.findChildCommentByCommentIdWithCursor(commentId, curCommentId);
    }

    /**
     * @param postId - 현재 댓글이 있는 게시물의 id
     * @param commentId - 가장 마지막으로 출력된 commentId
     * @return 현재 게시물에서 마지막 출력 commentId 보다 큰 Id를 가진 comment 10개
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findCommentByClickViewMore(Long postId, Long commentId){
        return commentRepository.findCommentByPostIdWithCursor(postId, commentId);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id){
        return commentRepository.findById(id).orElse(null);
    }

    private void isPostExist(Post post){
        if(post == null){
            throw new PostException("존재하지 않는 게시물입니다.");
        }
    }

    private void isMemberHaveAuthToUpdate(Comment comment ,Long memberId){
        if(comment == null){
            throw new CommentException("존재하지 않는 댓글입니다.");
        }
        if(comment.getMember().getId() != memberId){
            throw new CommentException("댓글 수정 / 삭제 권한이 없습니다.");
        }
    }
}
