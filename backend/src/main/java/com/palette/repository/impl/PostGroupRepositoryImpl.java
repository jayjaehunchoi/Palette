package com.palette.repository.impl;

import com.palette.dto.SearchCondition;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.repository.PostGroupRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class PostGroupRepositoryImpl implements PostGroupRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public PostGroupRepositoryImpl(EntityManager em){
        queryFactory = new JPAQueryFactory(em);
    }

    public List<PostGroupResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize) {

        queryFactory.select(Projections.fields(PostGroupR))

    }
}
