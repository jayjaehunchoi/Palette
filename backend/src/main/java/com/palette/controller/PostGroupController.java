package com.palette.controller;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.PostGroup;
import com.palette.dto.PeriodDto;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostGroupDto;
import com.palette.dto.response.PostGroupResponseDto;
import com.palette.dto.response.PostGroupsResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.dto.response.StoryListResponsesDto;
import com.palette.exception.PostGroupException;
import com.palette.service.PostGroupService;
import com.palette.service.PostService;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.ConstantUtil;
import com.palette.utils.S3Uploader;
import com.palette.controller.auth.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static com.palette.utils.constant.HttpResponseUtil.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/postgroup")
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
    @GetMapping("/my")
    public ResponseEntity<PostGroupsResponseDto> getMyPostGroup(@AuthenticationPrincipal Member member, @RequestParam(defaultValue = "1", required = false) int page){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setMemberId(member.getId());
        List<PostGroupResponseDto> postGroup = postGroupService.findPostGroup(searchCondition, page);
        PostGroupsResponseDto res = PostGroupsResponseDto.builder().postGroupResponses(postGroup).build();
        return ResponseEntity.ok(res);
    }

    @LoginChecker
    @PostMapping
    public ResponseEntity<Void> uploadPostGroup(@AuthenticationPrincipal Member member, @RequestPart("data")@Valid PostGroupDto dto, @RequestPart("file")MultipartFile file) throws IOException {
        isValidPeriod(dto.getPeriod());
        log.info("member {}",member);
        PostGroup postGroup = createPostGroupEntity(member, dto, file);
        PostGroup savePostGroup = postGroupService.createPostGroup(postGroup);
        return ResponseEntity.created(URI.create("/postgroup/"+savePostGroup.getId())).build();
    }

    @LoginChecker
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePostGroup(@AuthenticationPrincipal Member member, @PathVariable Long id, @RequestPart("data")@Valid PostGroupDto dto, @RequestPart("file") MultipartFile file) throws IOException{
        isValidPeriod(dto.getPeriod());
        postGroupService.checkMemberAuth(member,id);
        String storeFileName = postGroupService.getStoreFileNameIfChanged(id, file);
        MyFile myFile = updateDirectoryFile(file, storeFileName);
        postGroupService.updatePostGroup(id,dto,myFile);
        return RESPONSE_OK;
    }

    @LoginChecker
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostGroup(@AuthenticationPrincipal Member member, @PathVariable Long id){
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

    private void isValidPeriod(PeriodDto dto){
        if(dto.getEndDate().isBefore(dto.getStartDate())){
            throw new PostGroupException("시작일자와 종료일자를 다시 확인해주세요.");
        }
    }

}
