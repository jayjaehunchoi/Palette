package com.palette.repository.impl;


import com.palette.domain.member.QMember;
import com.palette.domain.post.QComment;
import com.palette.dto.response.CommentResponseDto;
import com.palette.repository.CommentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.palette.domain.member.QMember.*;
import static com.palette.domain.post.QComment.*;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public CommentRepositoryImpl (EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }
    private final static int COMMENT_SIZE = 10;

    // 댓글 커서 페이징 조회
    @Override
    public List<CommentResponseDto> findCommentByPostIdWithCursor(Long postId, Long commentId){
       return queryFactory.select(Projections.constructor(CommentResponseDto.class,
                member.id.as("memberId"),
                member.uname,
                comment.id.as("commentId"),
                comment.commentContent,
                comment.modifiedDate))
                .from(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.post.id.eq(postId), comment.parentCommentId.eq(0L) ,goeCommentId(commentId))
                .limit(COMMENT_SIZE)
                .fetch();
    }

    // 답글 커서 페이징 조회
    @Override
    public List<CommentResponseDto> findChildCommentByCommentIdWithCursor(Long commentId){
        return queryFactory.select(Projections.constructor(CommentResponseDto.class,
                        member.id.as("memberId"),
                        member.uname,
                        comment.id.as("commentId"),
                        comment.commentContent,
                        comment.modifiedDate))
                .from(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.parentCommentId.eq(commentId),goeCommentId(commentId))
                .limit(COMMENT_SIZE)
                .fetch();
    }

    // 초기 값 null일때 확인
    private BooleanExpression goeCommentId(Long commentId) {
        return commentId != null ? comment.id.goe(commentId) : null;
    }


}
