package com.palette.controller;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.PostGroup;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.dto.response.PostGroupsResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.dto.response.StoryListResponsesDto;
import com.palette.service.PostGroupService;
import com.palette.service.PostService;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.ConstantUtil;
import com.palette.utils.S3Uploader;
import com.palette.utils.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.palette.utils.constant.HttpResponseUtil.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/postgroup")
@RestController
public class PostGroupController {

    private final PostGroupService postGroupService;
    private final S3Uploader s3Uploader;
    private final PostService postService;

    // /postgroup?filter={필터}&condition={조건}&page={pageNumber}
    @GetMapping
    public ResponseEntity<PostGroupsResponseDto> getGroupPostWithFilter(@ModelAttribute SearchCondition searchCondition, @RequestParam(defaultValue = "1", required = false) int page){
        List<PostGroupResponseDto> postGroup = postGroupService.findPostGroup(searchCondition,page);
        PostGroupsResponseDto res = PostGroupsResponseDto.builder().postGroupResponses(postGroup).build();
        return ResponseEntity.ok(res);
    }

    // PostGroup 내의 Post보기
    @GetMapping("/{id}")
    public ResponseEntity<StoryListResponsesDto> getSingleGroupPost(@PathVariable Long id, @RequestParam(defaultValue = ConstantUtil.DEFAULT_PAGE, required = false) int page){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPostGroupId(id);
        List<StoryListResponseDto> storyList = postService.findStoryList(searchCondition, page);
        StoryListResponsesDto res = StoryListResponsesDto.builder().storyLists(storyList).build();
        return ResponseEntity.ok(res);
    }

    @LoginChecker
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long uploadPostGroup(@Login Member member, @RequestPart("data")@Valid PostGroupDto dto, @RequestPart("file")MultipartFile file) throws IOException {
        PostGroup postGroup = createPostGroupEntity(member, dto, file);
        PostGroup savePostGroup = postGroupService.createPostGroup(postGroup);
        return savePostGroup.getId();
    }

    @LoginChecker
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePostGroup(@Login Member member, @PathVariable Long id, @RequestPart("data")@Valid PostGroupDto dto, @RequestPart("file") MultipartFile file) throws IOException{
        postGroupService.checkMemberAuth(member,id);
        String storeFileName = postGroupService.getStoreFileNameIfChanged(id, file);
        MyFile myFile = updateDirectoryFile(file, storeFileName);
        postGroupService.updatePostGroup(id,dto,myFile);
        return RESPONSE_OK;
    }

    @LoginChecker
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostGroup(@Login Member member, @PathVariable Long id){
        PostGroup postGroup = postGroupService.checkMemberAuth(member, id);
        postService.findPostIdsByPostGroupId(id).forEach(postId -> postService.delete(postId));
        postGroupService.deletePostGroup(postGroup);
        return RESPONSE_OK;
    }

    private MyFile updateDirectoryFile(MultipartFile file, String storeFileName) throws IOException {
        if(storeFileName != null){
            s3Uploader.deleteS3(Arrays.asList(storeFileName));
            return s3Uploader.uploadSingleFile(file);
        }
        log.info("Group update 파일 없음");
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
