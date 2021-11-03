package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.exception.PaletteException;
import com.palette.exception.PostGroupException;
import com.palette.repository.PostGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class PostGroupService {
    private final int PAGE_SIZE = 10;
    private final PostGroupRepository postGroupRepository;

    @Transactional
    public void createPostGroup(PostGroup postGroup){
        postGroupRepository.save(postGroup);
    }

    @Transactional
    public void updatePostGroup(Long id, PostGroupDto dto){
        PostGroup findPostGroup = postGroupRepository.findById(id).orElse(null);
        findPostGroup.update(dto);
        log.info("제목 = {} 지역 = {}",findPostGroup.getTitle(), findPostGroup.getRegion());
    }
    @Transactional
    public void deletePostGroup(Long id){
        isPostGroupExist(id);
        postGroupRepository.deleteById(id);
    }

    // 멤버 아이디 조건으로 그룹 조회 (마이 블로그에서 활용 가능)
    public List<PostGroupResponseDto> findPostGroupByMember(String memberName, int pageNo){
        SearchCondition searchCondition = setSearchCondFilterMemberName(memberName);
        return postGroupRepository.findStoryListWithPage(searchCondition, pageNo, PAGE_SIZE);
    }

    // 지역 조건으로 그룹 조회 (스토리 검색 기능)
    public List<PostGroupResponseDto> findPostGroupByRegion(String region, int pageNo){
        SearchCondition searchCondition = setSearchCondFilterRegion(region);
        return postGroupRepository.findStoryListWithPage(searchCondition, pageNo, PAGE_SIZE);
    }

    // 그룹 타이틀 조건으로 그룹 조회 (스토리 검색 기능)
    public List<PostGroupResponseDto> findPostGroupByTitle(String title, int pageNo){
        SearchCondition searchCondition = setSearchCondFilterTitle(title);
        return postGroupRepository.findStoryListWithPage(searchCondition, pageNo, PAGE_SIZE);
    }

    // 조건 없이 그룹 조회
    public List<PostGroupResponseDto> findPostGroup(int pageNo){
        return postGroupRepository.findStoryListWithPage(new SearchCondition(), pageNo, PAGE_SIZE);
    }

    public PostGroup findById(Long id){
        return postGroupRepository.findById(id).orElse(null);
    }

    public List<PostGroup> findAll(){
        return postGroupRepository.findAll();
    }

    private void isPostGroupExist(Long id){
        if(findById(id) == null){
            log.error("Post Group Not Exist Error");
            throw new PostGroupException("존재하지 않는 게시물 그룹입니다.");
        }
    }



    private SearchCondition setSearchCondFilterMemberName(String memberName) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setName(memberName);
        return searchCondition;
    }

    private SearchCondition setSearchCondFilterRegion(String region) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setRegion(region);
        return searchCondition;
    }

    private SearchCondition setSearchCondFilterTitle(String groupTitle){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setTitle(groupTitle);
        return searchCondition;
    }

}
