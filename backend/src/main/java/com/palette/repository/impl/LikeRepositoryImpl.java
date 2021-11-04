package com.palette.repository.impl;

import com.palette.domain.member.Member;
import com.palette.domain.member.QMember;
import com.palette.repository.LikeRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.palette.domain.member.QMember.*;
import static com.palette.domain.post.QComment.comment;
import static com.palette.domain.post.QLike.like;

public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private final int LIKE_SIZE = 30;
    public LikeRepositoryImpl (EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }

    // 좋아요한 멤버 조회 (무한 스크롤 , 30개 단위, 마지막 출력 id 저장해서 스크롤 정점에 다다를때 다시 request)
    public List<Member> findLikeMemberByPost(Long postId, Long likeId){
        return queryFactory.select(member)
                .from(like)
                .join(like.member, member).fetchJoin()
                .where(like.post.id.eq(postId), ltLikeId(likeId))
                .orderBy(like.id.desc())
                .limit(LIKE_SIZE)
                .fetch();
    }

    private BooleanExpression ltLikeId(Long likeId) {
        return likeId != null ? like.id.goe(likeId) : null;
    }
}
