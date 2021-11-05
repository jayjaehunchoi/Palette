package com.palette.controller;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.PostGroup;
import com.palette.dto.GeneralResponse;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.service.PostGroupService;
import com.palette.service.PostService;
import com.palette.utils.S3Uploader;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.palette.utils.HttpResponseUtil.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/postgroup")
@RestController
public class PostGroupController {

    private final PostGroupService postGroupService;
    private final S3Uploader s3Uploader;
    private final PostService postService;

    // /postgroup?page={pageNumber}
    // 1페이지당 9개 그룹 출력
    @GetMapping
    public ResponseEntity<GeneralResponse> getGroupPostWithoutFilter(@RequestParam(defaultValue = "1", required = false) int page){
        List<PostGroupResponseDto> postGroup = postGroupService.findPostGroup(page);
        GeneralResponse<Object> res = GeneralResponse.builder().data(postGroup).build();
        return ResponseEntity.ok(res);
    }

    // /postgroup/{필터}/{필터 조건}?page={pageNumber}
    @GetMapping("/{filter}/{condition}")
    public ResponseEntity<GeneralResponse> getGroupPostWithFilter(@PathVariable String filter, @PathVariable String condition, @RequestParam(defaultValue = "1", required = false) int page){
        List<PostGroupResponseDto> postGroup = null;
        switch (filter){
            case "member" :
                postGroup = postGroupService.findPostGroupByMember(condition, page);
                break;
            case "region" :
                postGroup = postGroupService.findPostGroupByRegion(condition, page);
                break;
            case "title" :
                postGroup = postGroupService.findPostGroupByTitle(condition, page);
        }
        GeneralResponse<Object> res = GeneralResponse.builder().data(postGroup).build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getSingleGroupPost(@PathVariable Long id){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPostGroupId(id);
        List<StoryListResponseDto> storyList = postService.findStoryList(searchCondition, 1);
        GeneralResponse<Object> res = GeneralResponse.builder().data(storyList).build();
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<Void> uploadPostGroup(@Login Member member, @RequestPart(value = "data")PostGroupDto dto, @RequestPart(value = "file")MultipartFile file) throws IOException {
        PostGroup postGroup = createPostGroupEntity(member, dto, file);
        postGroupService.createPostGroup(postGroup);
        return RESPONSE_CREATED;
    }

    // 로그인 체크 꼭 필요
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePostGroup(@Login Member member, @PathVariable Long id, @RequestPart(value = "data") PostGroupDto dto, @RequestPart(value = "file") MultipartFile file) throws IOException{
        postGroupService.checkMemberAuth(member,id);
        String storeFileName = postGroupService.getStoreFileNameIfChanged(id, file);
        MyFile myFile = updateDirectoryFile(file, storeFileName);
        postGroupService.updatePostGroup(id,dto,myFile);
        return RESPONSE_OK;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostGroup(@Login Member member, @PathVariable Long id){
        postGroupService.checkMemberAuth(member,id);
        postGroupService.deletePostGroup(id);
        return RESPONSE_OK;
    }

    private MyFile updateDirectoryFile(MultipartFile file, String storeFileName) throws IOException {
        if(storeFileName != null){
            s3Uploader.deleteS3(Arrays.asList(storeFileName));
            return s3Uploader.uploadSingleFile(file);
        }
        return null;
    }

    private PostGroup createPostGroupEntity(Member member, PostGroupDto dto, MultipartFile file) throws IOException {
        MyFile myFile = s3Uploader.uploadSingleFile(file);
        PostGroup postGroup = PostGroup.builder()
                .title(dto.getTitle())
                .member(member)
                .period(new Period(dto.getPeriod()))
                .region(dto.getRegion())
                .build();
        postGroup.setThumbNail(myFile);
        return postGroup;
    }

}
