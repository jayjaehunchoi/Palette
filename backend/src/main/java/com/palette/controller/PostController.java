package com.palette.controller;

import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.Post;
import com.palette.domain.post.PostGroup;
import com.palette.dto.GeneralResponse;
import com.palette.dto.SearchCondition;
import com.palette.dto.request.PostRequestDto;
import com.palette.dto.response.*;
import com.palette.service.LikeService;
import com.palette.service.PostGroupService;
import com.palette.service.PostService;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.ConstantUtil;
import com.palette.utils.constant.HttpResponseUtil;
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
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final PostGroupService postGroupService;
    private final S3Uploader s3Uploader;
    private final LikeService likeService;

    // 그룹을 안거치고 조회시, 그룹 거친 조회는 PostGroup에 존재
    @GetMapping("/post")
    public ResponseEntity<StoryListResponsesDto> getPosts(@ModelAttribute SearchCondition searchCondition, @RequestParam(defaultValue = ConstantUtil.DEFAULT_PAGE,required = false) int page){
        List<StoryListResponseDto> storyList = postService.findStoryList(searchCondition, page);
        StoryListResponsesDto res = StoryListResponsesDto.builder().storyLists(storyList).build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/post/{id}")
    public PostResponseDto getSinglePost(@PathVariable Long id){
        return postService.findSinglePost(id, ConstantUtil.INIT_ID);
    }

    @GetMapping("/post/{id}/like")
    public ResponseEntity<LikeResponsesDto> getLikeMembers(@PathVariable Long id, @RequestParam(required = false) Long likeId){
        List<Member> likeMemberByPost = likeService.findLikeMemberByPost(id, likeId);
        List<LikeResponseDto> likeResponseDtos = likeMemberByPost.stream().map(LikeResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(LikeResponsesDto.builder().likeResponses(likeResponseDtos).build());
    }

    @LoginChecker
    @PostMapping("/post/{id}/like")
    public ResponseEntity<GeneralResponse> pushLikeButton(@Login Member member, @PathVariable Long id){
        int likeCount = likeService.pushLike(member, id);
        return ResponseEntity.ok(GeneralResponse.builder().data(likeCount).build());
    }

    @LoginChecker
    @PostMapping("/postgroup/{postGroupId}/post")
    public ResponseEntity<Void> createPost(@Login Member member, @PathVariable Long postGroupId, @RequestPart("data")@Valid PostRequestDto postRequestDto, @RequestPart("files") List<MultipartFile> imageFiles) throws IOException {
        PostGroup findPostGroup = postGroupService.findById(postGroupId);
        postService.isAvailablePostOnPostGroup(findPostGroup, member.getId());
        List<MyFile> myFiles = s3Uploader.uploadFiles(imageFiles);
        Post post = Post.builder().title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .member(member)
                .region(findPostGroup.getRegion())
                .period(findPostGroup.getPeriod())
                .build();
        Post savedPost = postService.write(post, findPostGroup, myFiles);
        return ResponseEntity.created(URI.create("/post/"+savedPost.getId())).build();
    }

    @LoginChecker
    @PutMapping("/postgroup/{postGroupId}/post/{id}")
    public ResponseEntity<Void> updatePost(@Login Member member, @PathVariable("postGroupId") Long postGroupId, @PathVariable("id") Long postId, @RequestBody @Valid PostRequestDto postRequestDto){
        validateMemberCanUpdateOrDeletePost(member, postGroupId, postId);
        postService.update(postId,postRequestDto);
        return HttpResponseUtil.RESPONSE_OK;
    }

    @LoginChecker
    @DeleteMapping("/postgroup/{postGroupId}/post/{id}")
    public ResponseEntity<Void> deletePost(@Login Member member, @PathVariable("postGroupId") Long postGroupId, @PathVariable("id") Long postId){
        validateMemberCanUpdateOrDeletePost(member, postGroupId, postId);
        postService.delete(postId);
        return HttpResponseUtil.RESPONSE_OK;
    }

    private void validateMemberCanUpdateOrDeletePost(Member member, Long postGroupId, Long postId) {
        PostGroup findPostGroup = postGroupService.findById(postGroupId);
        Post findPost = postService.findById(postId);
        postService.isAvailablePostOnPostGroup(findPostGroup, member.getId());
        postService.isAvailableUpdatePost(findPost, member);
    }
}
