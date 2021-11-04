package com.palette.service;

import com.palette.domain.post.Comment;
import com.palette.domain.post.Post;
import com.palette.exception.CommentException;
import com.palette.exception.PostException;
import com.palette.repository.CommentRepository;
import com.palette.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void writeComment(Comment comment, Long postId, Long commentId){
        Post findPost = postRepository.findById(postId).orElse(null);
        isPostExist(findPost);
        comment.writeComment(findPost, commentId);
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
    }

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
