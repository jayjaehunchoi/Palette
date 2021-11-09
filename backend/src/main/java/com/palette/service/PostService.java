package com.palette.service;

import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.palette.domain.post.PostGroup;
import com.palette.dto.request.PostRequestDto;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.PostResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.exception.PostException;
import com.palette.repository.*;
import com.palette.utils.ConstantUtil;
import com.palette.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.palette.utils.ConstantUtil.PAGE_SIZE;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PhotoRepository photoRepository;
    private final S3Uploader s3Uploader;

    // 넘어온 사진이 없는 경우
    public Post write(Post post, PostGroup postGroup){
        post.createPostOnPostGroup(postGroup);
        // 기본 사진 세팅 필요 or 사진 없으면 글 작성 못하게 제한
        return postRepository.save(post);
    }


    // 넘어온 사진이 있는경우
    public Post write(Post post, PostGroup postGroup, List<MyFile> myFiles){
        post.createPostOnPostGroup(postGroup);

        for (MyFile myFile : myFiles) {
            Photo photo = new Photo(myFile);
            photo.setPicturesOnPost(post);
        }
        return postRepository.save(post);
    }

    public void delete(Long postId){
        Post findPost = postRepository.findById(postId).orElse(null);
        isPostExist(findPost);
        likeRepository.deleteAllLikeByPostId(postId);
        List<String> storedFileList = findPost.getPhotos().stream().map(photo -> photo.getFile().getStoreFileName()).collect(Collectors.toList());
        s3Uploader.deleteS3(storedFileList);
        commentRepository.deleteAllCommentByPostId(postId);
        photoRepository.deleteAllPhotoByPostId(postId);
        postRepository.deleteById(postId);
    }

    // todo : update 가능 목록 이야기 필요
    public void update(Long postId, PostRequestDto dto){
        Post findPost = postRepository.findById(postId).orElse(null);
        isPostExist(findPost);
        findPost.update(dto);
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public PostResponseDto findSinglePost(Long postId, Long commentId){
        Post findPost = postRepository.findSinglePost(postId);
        isPostExist(findPost);
        findPost.visitPost();
        List<String> images = creatFullPathImageList(findPost); // 이미지 풀경로 리스트
        PostResponseDto postResponseDto = new PostResponseDto(findPost);
        postResponseDto.setComments(commentRepository.findCommentByPostIdWithCursor(postId, commentId)); // 최초 조회시 comment
        postResponseDto.setImages(images);

        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public Post findById(Long id){
        Post findPost = postRepository.findById(id).orElse(null);
        isPostExist(findPost);
        return findPost;
    }

    public List<Long> findPostIdsByPostGroupId(Long postGroupId){
        List<Post> posts = postRepository.findByPostGroupId(postGroupId);
        return posts.stream().map(post -> post.getId()).collect(Collectors.toList());
    }

    // storyList 페이징 출력
    @Transactional(readOnly = true)
    public List<StoryListResponseDto> findStoryList(SearchCondition condition, int pageNo) {
        List<StoryListResponseDto> results = postRepository.findStoryListWithPage(condition, pageNo, ConstantUtil.PAGE_SIZE);
        List<Long> postIds = results.stream().map(result -> result.getPostId()).collect(Collectors.toList());
        Map<Long, String> thumbnailMap = postRepository.findThumbnailByPostId(postIds);
        updateStoryListResponseDto(results, thumbnailMap);
        return results;
    }
    public List<String> creatFullPathImageList(Post findPost) {
        return findPost.getPhotos().stream()
                .map(photo -> photo.getFile().getStoreFileName())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getTotalPage(SearchCondition condition){
        return ((postRepository.getPostTotalCount(condition) - 1) / PAGE_SIZE)+1;
    }

    private void updateStoryListResponseDto(List<StoryListResponseDto> results,  Map<Long, String> thumbnailMap) {
        results.forEach(result -> {
            String path = null;
            path = thumbnailMap.get(result.getPostId());
            result.setThumbNailFilePath(path);
        });
    }

    public void isAvailablePostOnPostGroup(PostGroup postGroup, Long memberId) {
        if(postGroup.getMember().getId() != memberId){
            throw new PostException("그룹 내 게시물 작성 / 수정 / 삭제 권한이 없습니다.");
        }
    }

    public void isAvailableUpdatePost(Post findPost, Member member){
        if(member.getId() != findPost.getMember().getId()){
            throw new PostException("게시물 수정 / 삭제 권한이 없습니다.");
        }
    }

    private void isPostExist(Post findPost) {
        if(findPost == null){
            throw new PostException("게시물이 존재하지 않습니다.");
        }
    }


}
