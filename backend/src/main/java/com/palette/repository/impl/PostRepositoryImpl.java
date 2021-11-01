package com.palette.repository.impl;

import com.palette.domain.member.QMember;
import com.palette.domain.post.QPhoto;
import com.palette.domain.post.QPost;
import com.palette.dto.PeriodDto;
import com.palette.dto.QStoryListResponseDto;
import com.palette.dto.SearchCondition;
import com.palette.dto.StoryListResponseDto;
import com.palette.repository.PostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.palette.domain.member.QMember.*;
import static com.palette.domain.post.QPhoto.*;
import static com.palette.domain.post.QPost.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public PostRepositoryImpl (EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<StoryListResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize) {
        List<Long> postIds = queryFactory.select(post.id)
                .from(post)
                .leftJoin(post.member, member).fetchJoin() // 1:n 조회는 페이징 불가, 따라서 페치 조인은 단건까지
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
        return queryFactory.select(new QStoryListResponseDto( post.member.id.as("memberId"),
                        post.member.uname,
                        post.id.as("postId"),
                        post.photos.get(0).storeFileName,
                        post.title,
                        post.likes.size(),
                        post.period.startDate,
                        post.period.endDate))
                        .from(post)
                        .where(post.id.in(postIds))
                        .orderBy(post.id.desc())
                        .fetch();
    }

    private BooleanExpression memberNameEq(String uname){
        return hasText(uname) ? member.uname.eq(uname):null;
    }
    private BooleanExpression regionEq(String region){
        return hasText(region) ? post.region.eq(region) : null;
    }
    private BooleanExpression titleContain(String title){
        return hasText(title) ? post.title.contains(title) : null; // string 값 포함 검색값 조회
    }
}
