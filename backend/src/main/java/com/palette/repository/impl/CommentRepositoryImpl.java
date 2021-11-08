package com.palette.repository.impl;


import com.palette.dto.response.CommentResponseDto;
import com.palette.repository.CommentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.palette.domain.member.QMember.*;
import static com.palette.domain.post.QComment.*;
import static com.palette.utils.ConstantUtil.COMMENT_SIZE;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public CommentRepositoryImpl (EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }


    // dto로 join 하는경우 fetch join이 아닌 일반 join
    // 댓글 커서 페이징 조회
    @Override
    public List<CommentResponseDto> findCommentByPostIdWithCursor(Long postId, Long commentId){
       return queryFactory.select(Projections.constructor(CommentResponseDto.class,
                member.id.as("memberId"),
                member.name,
                comment.id.as("commentId"),
                comment.commentContent,
                comment.modifiedDate))
                .from(comment)
                .join(comment.member, member)
                .where(comment.post.id.eq(postId), comment.parentCommentId.eq(0L) , gtCommentId(commentId))
                .limit(COMMENT_SIZE)
                .fetch();
    }

    // 답글 커서 페이징 조회
    @Override
    public List<CommentResponseDto> findChildCommentByCommentIdWithCursor(Long commentId, Long curCommentId){
        return queryFactory.select(Projections.constructor(CommentResponseDto.class,
                        member.id.as("memberId"),
                        member.name,
                        comment.id.as("commentId"),
                        comment.commentContent,
                        comment.modifiedDate))
                .from(comment)
                .join(comment.member, member)
                .where(comment.parentCommentId.eq(commentId), gtCommentId(curCommentId))
                .limit(COMMENT_SIZE)
                .fetch();
    }

    // 초기 값 null일때 확인
    private BooleanExpression gtCommentId(Long commentId) {
        return commentId != null ? comment.id.gt(commentId) : null;
    }


}
