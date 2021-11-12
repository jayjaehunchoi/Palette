package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.PostGroup;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.exception.PostGroupException;
import com.palette.repository.PostGroupRepository;
import com.palette.repository.PostRepository;
import com.palette.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.palette.utils.constant.ConstantUtil.*;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class PostGroupService {

    private final PostGroupRepository postGroupRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public PostGroup createPostGroup(PostGroup postGroup){
        return postGroupRepository.save(postGroup);
    }

    @Transactional
    public void updatePostGroup(Long id, PostGroupDto dto, MyFile myFile){
        PostGroup findPostGroup = postGroupRepository.findById(id).orElse(null);
        isPostGroupExist(findPostGroup);
        findPostGroup.update(dto);
        if(myFile != null){
            findPostGroup.setThumbNail(myFile);
        }
        log.info("제목 = {} 지역 = {}",findPostGroup.getTitle(), findPostGroup.getRegion());
    }

    // check member auth 실행 후 사용
    @Transactional
    public void deletePostGroup(PostGroup postGroup){
        s3Uploader.deleteS3(Arrays.asList(postGroup.getThumbNail().getStoreFileName()));
        postGroupRepository.deleteById(postGroup.getId());
    }


    public String getStoreFileNameIfChanged(Long id,MultipartFile multipartFile){
        PostGroup findPostGroup = postGroupRepository.findById(id).orElse(null);
        isPostGroupExist(findPostGroup);
        boolean isNotChanged = multipartFile.getOriginalFilename().equals(findPostGroup.getThumbNail().getUploadFileName());
        if(!isNotChanged){
            return findPostGroup.getThumbNail().getStoreFileName();
        }
        return null;
    }

    // 파일 업로드 , 업데이트 , 삭제 이슈때문에 따로 검증 로직 빼줌.
    public PostGroup checkMemberAuth(Member member, Long id){
        PostGroup findPostGroup = postGroupRepository.findById(id).orElse(null);
        isPostGroupExist(findPostGroup);
        if(findPostGroup.getMember().getId() != member.getId()){
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "권한이 없습니다."){};
        }
        return findPostGroup;
    }

    // 조건 없거나, 멤버 조건만으로 그룹 조회
    public List<PostGroupResponseDto> findPostGroup(SearchCondition searchCondition,int pageNo){
        return postGroupRepository.findStoryListWithPage(searchCondition, pageNo, PAGE_SIZE);
    }

    public PostGroup findById(Long id){
        PostGroup postGroup = postGroupRepository.findById(id).orElse(null);
        isPostGroupExist(postGroup);
        return postGroup;
    }

    public List<PostGroup> findAll(){
        return postGroupRepository.findAll();
    }

    // 페이지 수 조회
    public long getTotalPage(SearchCondition searchCondition){
        return ((postGroupRepository.getStoryListTotalCount(searchCondition) - 1) / PAGE_SIZE)+1;
    }

    private void isPostGroupExist(PostGroup findPostGroup) {
        if (findPostGroup == null) {
            log.error("Post Group Not Exist Error");
            throw new PostGroupException("존재하지 않는 게시물 그룹입니다.");
        }
    }

}
