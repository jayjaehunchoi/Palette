package com.palette.repository.impl;

import com.palette.domain.post.Post;
import com.palette.dto.*;
import com.palette.dto.response.QStoryListResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.repository.PostRepositoryCustom;
import com.palette.utils.ConstantUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.palette.domain.post.QPhoto.*;
import static com.palette.domain.post.QPost.*;
import static com.palette.utils.ConstantUtil.*;
import static org.springframework.util.StringUtils.*;

@Repository
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
                        titleContain(condition.getTitle()),
                        postGroupIdEq(condition.getPostGroupId()))
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
                post.title,
                post.likeCount))
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
                .orderBy(photo.id.asc())
                .fetch();

        for (Tuple tuple : result) {
            if(baseMap.get(tuple.get(0,Long.class)).equals(BASIC_THUMBNAIL)){
                baseMap.replace(tuple.get(0,Long.class), tuple.get(1,String.class));
            }
        }
        return baseMap;
    }

    // Comment는 페이징을 위해 따로 조회해서 사용할 것.
    // Like 개수도 따로 조회해서 사용할 것.
    public Post findSinglePost(Long postId){
        List<Post> posts = queryFactory.select(post).distinct()
                .from(post)
                .join(post.photos,photo).fetchJoin()
                .where(post.id.eq(postId))
                .fetch();

        return posts.get(0);
    }

    private Map<Long, String> createBaseMap(List<Long> postIds) {
        Map<Long, String> baseThumbnailMap = new HashMap<>();
        for (Long postId : postIds) {
            baseThumbnailMap.put(postId,BASIC_THUMBNAIL);
        }
        return baseThumbnailMap;
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
    private BooleanExpression postGroupIdEq(Long postGroupId){
        return postGroupId != null ? post.postGroup.id.eq(postGroupId) : null;
    }
}
