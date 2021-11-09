package com.palette.repository;

import com.palette.domain.post.Post;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.StoryListResponseDto;

import java.util.List;
import java.util.Map;

public interface PostRepositoryCustom {

    List<StoryListResponseDto> findStoryListWithPage(SearchCondition condition, int pageNo, int pageSize);
    Map<Long, String> findThumbnailByPostId(List<Long> postIds);
    Post findSinglePost(Long postId);
    long getPostTotalCount(SearchCondition condition);
}
