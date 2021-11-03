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

import java.util.Collections;
import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class PostGroupService {
    private final int PAGE_SIZE = 10;
    private final PostGroupRepository postGroupRepository;

    public void createPostGroup(PostGroup postGroup){
        postGroupRepository.save(postGroup);
    }


    public PostGroup updatePostGroup(Long id, PostGroupDto dto){
        PostGroup findPostGroup = findById(id);
        findPostGroup.update(dto);
        return findPostGroup;
    }

    public void deletePostGroup(Long id){
        isPostGroupExist(id);
        postGroupRepository.deleteById(id);
    }

    // 멤버 아이디 조건으로 그룹 조회 (마이 블로그에서 활용 가능)
    @Transactional(readOnly = true)
    public List<PostGroupResponseDto> findPostGroupByMember(Member member, int pageNo){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setName(member.getUname());
        return postGroupRepository.findStoryListWithPage(searchCondition, pageNo, PAGE_SIZE);
    }

    // 조건 없이 그룹 조회
    @Transactional(readOnly = true)
    public List<PostGroupResponseDto> findPostGroup(int pageNo){
        return postGroupRepository.findStoryListWithPage(new SearchCondition(), pageNo, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    public PostGroup findById(Long id){
        return postGroupRepository.findById(id).orElse(null);
    }

    private void isPostGroupExist(Long id){
        if(findById(id) == null){
            throw new PostGroupException("존재하지 않는 게시물 그룹입니다.");
        }
    }
}
