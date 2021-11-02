package com.palette.repository.impl;

import com.palette.dto.*;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.repository.PostRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.palette.domain.post.QLike.*;
import static com.palette.domain.post.QPhoto.*;
import static com.palette.domain.post.QPost.*;
import static org.springframework.util.StringUtils.*;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public PostRepositoryImpl (EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<StoryListResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize) {
        // 페이징 성능 개선을 위한 index 기반 조회
        List<Long> postIds = queryFactory.select(post.id)
                .from(post)// 1:n 조회는 페이징 불가, 따라서 페치 조인은 단건까지
                .where(memberNameEq(condition.getName()),
                        regionEq(condition.getRegion()),
                        titleContain(condition.getTitle()))
                .orderBy(post.id.desc())
                .offset(pageNo-1)
                .limit(pageSize)
                .fetch();

        if(CollectionUtils.isEmpty(postIds)){
            return new ArrayList<>();
        }
        return queryFactory.select(new QStoryListResponseDto(post.member.id.as("memberId"),
                post.member.uname,
                post.id.as("postId"),
                post.title))
                        .from(post)
                        .where(post.id.in(postIds))
                        .orderBy(post.id.desc())
                        .fetch();
    }

    // 일대다 연관관계 조회, 1+N 문제 해결
    @Override
    public Map<Long, String> findThumbnailByPostId(List<Long> postIds){
        Map<Long, String> baseMap = createBaseMap(postIds); // 이미지를 저장하지 않는 게시글에 대한 기본 썸네일 적용

        List<Tuple> result = queryFactory.select(photo.post.id, photo.file.storeFileName)
                .from(photo)
                .where(photo.post.id.in(postIds))
                .limit(1)
                .fetch();

        for (Tuple tuple : result) {
            baseMap.replace(tuple.get(0,Long.class), tuple.get(1,String.class));
        }
        return baseMap;
    }

    // todo : 기본 썸네일 파일 지정 후 파일 명 update 필요
    private Map<Long, String> createBaseMap(List<Long> postIds) {
        Map<Long, String> baseThumbnailMap = new HashMap<>();
        for (Long postId : postIds) {
            baseThumbnailMap.put(postId,"기본썸네일");
        }
        return baseThumbnailMap;
    }

    // 일대다 연관관계 조회, 1+N 문제 해결
    @Override
    public Map<Long, Long> findLikesCountByPostId(List<Long> postIds){
        List<Tuple> tuple = queryFactory.select(like.post.id, like.post.id.count())
                .from(like)
                .groupBy(like.post.id)
                .where(like.post.id.in(postIds))
                .fetch();

        Map<Long, Long> likeCounts = new HashMap<>();
        for (Tuple tup : tuple) {
            likeCounts.put(tup.get(0,Long.class), tup.get(1,Long.class));
        }
        return likeCounts;
    }

    // 동적 쿼리 작성용
    private BooleanExpression memberNameEq(String uname){
        return hasText(uname) ? post.member.uname.eq(uname):null;
    }
    private BooleanExpression regionEq(String region){
        return hasText(region) ? post.region.eq(region) : null;
    }
    private BooleanExpression titleContain(String title){
        return hasText(title) ? post.title.contains(title) : null; // string 값 포함 검색값 조회
    }
}
