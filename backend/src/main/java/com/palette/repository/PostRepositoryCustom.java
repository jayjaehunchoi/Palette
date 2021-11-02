package com.palette.repository;

import com.palette.dto.SearchCondition;
import com.palette.dto.response.StoryListResponseDto;

import java.util.List;
import java.util.Map;

public interface PostRepositoryCustom {

    List<StoryListResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize);
    Map<Long, String> findThumbnailByPostId(List<Long> postIds);
    Map<Long, Long> findLikesCountByPostId(List<Long> postIds);

}
