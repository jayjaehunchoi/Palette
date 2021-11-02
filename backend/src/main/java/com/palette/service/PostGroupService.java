package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.domain.post.PostGroup;
import com.palette.dto.request.PostGroupDto;
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

    public List<PostGroup> findPostGroupByMember(Member member){
        return postGroupRepository.findByMember(member).orElse(Collections.emptyList());
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
