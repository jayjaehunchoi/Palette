package com.palette.repository;

import com.palette.dto.SearchCondition;
import com.palette.dto.StoryListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<StoryListResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize);
}
