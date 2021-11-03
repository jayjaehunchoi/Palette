package com.palette.service;

import com.palette.domain.post.MyFile;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.palette.dto.request.PostUpdateDto;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.repository.LikeRepository;
import com.palette.repository.PhotoRepository;
import com.palette.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final FileStorageService fileStorageService;

    // 넘어온 사진이 없는 경우
    @Transactional
    public void write(Post post){
        postRepository.save(post);
    }

    // 넘어온 사진이 있는경우
    @Transactional
    public void write(Post post, List<MyFile> myFiles){
        for (MyFile myFile : myFiles) {
            Photo photo = new Photo(myFile);
            photo.setPicturesOnPost(post);
        }
        postRepository.save(post);
    }

    // todo : null check
    @Transactional
    public void delete(Long postId){
        postRepository.deleteById(postId);
    }

    // todo : null check , update 가능 목록 이야기 필요
    @Transactional
    public Post update(Long postId, PostUpdateDto dto){
        Post findPost = postRepository.findById(postId).orElse(null);
        findPost.update(dto);
        return findPost;
    }

    // storyList 페이징 출력
    public List<StoryListResponseDto> findStoryList(SearchCondition condition, int pageNo, int pageSize) {
        List<StoryListResponseDto> results = postRepository.findStoryListWithPage(condition, pageNo, pageSize);
        List<Long> postIds = results.stream().map(result -> result.getPostId()).collect(Collectors.toList());
        Map<Long, Long> likes = likeRepository.findLikesCountByPostId(postIds);
        Map<Long, String> thumbnailMap = postRepository.findThumbnailByPostId(postIds);
        updateStoryListResponseDto(results, likes, thumbnailMap);
        return results;
    }

    private void updateStoryListResponseDto(List<StoryListResponseDto> results, Map<Long, Long> likes, Map<Long, String> thumbnailMap) {
        results.forEach(result -> {
            UrlResource path = null;
            try {
                path = new UrlResource("file:" +fileStorageService.getFullPath(thumbnailMap.get(result.getPostId())));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            long likesCount = likes.getOrDefault(result.getPostId(),0L);
            result.setThumbNailFilePath(path);
            result.setLikesCount(likesCount);
        });
    }

}
