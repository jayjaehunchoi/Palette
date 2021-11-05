package com.palette.service;

import com.palette.domain.post.MyFile;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.palette.dto.request.PostUpdateDto;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.PostResponseDto;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.repository.CommentRepository;
import com.palette.repository.PostRepository;
import com.palette.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 넘어온 사진이 없는 경우
    public void write(Post post){
        postRepository.save(post);
    }

    // 넘어온 사진이 있는경우
    public void write(Post post, List<MyFile> myFiles){
        for (MyFile myFile : myFiles) {
            Photo photo = new Photo(myFile);
            photo.setPicturesOnPost(post);
        }
        postRepository.save(post);
    }

    // todo : null check
    public void delete(Long postId){
        postRepository.deleteById(postId);
    }

    // todo : null check , update 가능 목록 이야기 필요
    public Post update(Long postId, PostUpdateDto dto){
        Post findPost = postRepository.findById(postId).orElse(null);
        findPost.update(dto);
        return findPost;
    }

    // 단건 조회
    public PostResponseDto findSinglePost(Long postId, Long commentId){
        Post findPost = postRepository.findSinglePost(postId);

        List<String> images = creatFullPathImageList(findPost); // 이미지 풀경로 리스트
        PostResponseDto postResponseDto = new PostResponseDto(findPost);
        postResponseDto.setComments(commentRepository.findCommentByPostIdWithCursor(postId, commentId)); // 최초 조회시 comment
        postResponseDto.setImages(images);

        return postResponseDto;
    }


    // storyList 페이징 출력
    @Transactional(readOnly = true)
    public List<StoryListResponseDto> findStoryList(SearchCondition condition, int pageNo, int pageSize) {
        List<StoryListResponseDto> results = postRepository.findStoryListWithPage(condition, pageNo, pageSize);
        List<Long> postIds = results.stream().map(result -> result.getPostId()).collect(Collectors.toList());
        Map<Long, String> thumbnailMap = postRepository.findThumbnailByPostId(postIds);
        updateStoryListResponseDto(results, thumbnailMap);
        return results;
    }

    private void updateStoryListResponseDto(List<StoryListResponseDto> results,  Map<Long, String> thumbnailMap) {
        results.forEach(result -> {
            String path = null;
            path = thumbnailMap.get(result.getPostId());
            result.setThumbNailFilePath(path);
        });
    }
    private List<String> creatFullPathImageList(Post findPost) {
        return findPost.getPhotos().stream()
                .map(photo -> photo.getFile().getStoreFileName())
                .collect(Collectors.toList());
    }



}
