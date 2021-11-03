package com.palette.repository;

import com.palette.dto.SearchCondition;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.dto.response.StoryListResponseDto;

import java.util.List;
import java.util.Map;

public interface PostGroupRepositoryCustom {
    List<PostGroupResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize);

}
