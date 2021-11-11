package com.palette.repository.impl;


import com.palette.dto.SearchCondition;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.repository.PostGroupRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.palette.domain.member.QMember.*;
import static com.palette.domain.post.QPostGroup.*;
import static org.springframework.util.StringUtils.hasText;

public class PostGroupRepositoryImpl implements PostGroupRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public PostGroupRepositoryImpl(EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }

    public List<PostGroupResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize) {
        List<Long> postGroupIds = getPostGroupIdsWithIndex(condition, pageNo, pageSize);
        if(CollectionUtils.isEmpty(postGroupIds)){
            return null;
        }

        return queryFactory.select(Projections.constructor(PostGroupResponseDto.class,
                postGroup.id.as("postGroup_id"),
                postGroup.member.id.as("member_id"),
                postGroup.member.name,
                postGroup.title,
                postGroup.thumbNail.storeFileName,
                postGroup.period.startDate,
                postGroup.period.endDate,
                postGroup.region
                )).from(postGroup)
                .join(postGroup.member, member)
                .where(postGroup.id.in(postGroupIds))
                .orderBy(postGroup.id.desc())
                .fetch();
    }

    public long getStoryListTotalCount(SearchCondition condition){
        return queryFactory.select(postGroup.id)
                .from(postGroup)
                .where(memberNameEq(condition.getName()),
                        regionEq(condition.getRegion()),
                        titleContain(condition.getTitle()))
                .fetchCount();
    }

    private List<Long> getPostGroupIdsWithIndex(SearchCondition condition, int pageNo, int pageSize) {
        return queryFactory.select(postGroup.id)
                .from(postGroup)
                .join(postGroup.member, member)
                .where(memberNameEq(condition.getName()),
                        regionEq(condition.getRegion()),
                        titleContain(condition.getTitle()))
                .orderBy(postGroup.id.desc())
                .offset(pageNo - 1)
                .limit(pageSize)
                .fetch();

    }

    private BooleanExpression memberNameEq(String name){
        return hasText(name) ? postGroup.member.name.eq(name):null;
    }
    private BooleanExpression regionEq(String region){
        return hasText(region) ? postGroup.region.eq(region) : null;
    }
    private BooleanExpression titleContain(String title){
        return hasText(title) ? postGroup.title.contains(title) : null; // string 값 포함 검색값 조회
    }
}
