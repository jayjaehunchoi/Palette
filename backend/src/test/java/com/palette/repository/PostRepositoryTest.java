package com.palette.repository;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.domain.post.MyFile;
import com.palette.domain.post.Photo;
import com.palette.domain.post.Post;
import com.palette.dto.SearchCondition;
import com.palette.dto.response.StoryListResponseDto;
import com.palette.utils.constant.ConstantUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.palette.utils.constant.ConstantUtil.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class PostRepositoryTest {
    
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        Member member = new Member("wogns", "1234", "wogns","123");
        memberRepository.save(member);

        Member findMember = memberRepository.findAll().get(0);

        for(int i = 0 ; i < 30 ; i++){
            String region = "서울";
            if(i > 15 )region = "부산";
            Post post = Post.builder().title("제목입니다" + i)
                    .member(findMember)
                    .content("내용")
                    .region(region)
                    .period(new Period(LocalDate.of(2021, 11, 2)
                            , LocalDate.of(2021, 11, 5)))
                    .build();
            for(int j = 0 ; j < 3; j++){
                Photo photo = new Photo(new MyFile("abc"+i, "abc"+i));
                photo.setPicturesOnPost(post);
            }

            postRepository.save(post);
        }

    }

    @Test
    void 이름으로_검색(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setName("wogns");
        List<StoryListResponseDto> stories = postRepository.findStoryListWithPage(searchCondition, 1, 10);

        assertThat(stories.size()).isEqualTo(10);
    }

    @Test
    void 서울으로_검색(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setRegion("서울");
        List<StoryListResponseDto> stories = postRepository.findStoryListWithPage(searchCondition, 1, 10);

        int i = 15;
        for (StoryListResponseDto story : stories) {
            assertThat(story.getTitle()).isEqualTo("제목입니다"+i);
            i--;
        }
    }

    @Test
    void 부산으로_검색(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setRegion("부산");
        List<StoryListResponseDto> stories = postRepository.findStoryListWithPage(searchCondition, 1, 10);

        int i = 29;
        for (StoryListResponseDto story : stories) {
            assertThat(story.getTitle()).isEqualTo("제목입니다"+i);
            i--;
        }
    }

    @Test
    void 제목으로_검색(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setTitle("입니다");
        List<StoryListResponseDto> stories = postRepository.findStoryListWithPage(searchCondition, 1, 10);
        int i = 29;
        for (StoryListResponseDto story : stories) {
            assertThat(story.getTitle()).isEqualTo("제목입니다"+i);
            i--;
        }
        assertThat(stories.size()).isEqualTo(10);
    }

    @Test
    void 제목_두글자로_검색(){
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setTitle("22");
        List<StoryListResponseDto> stories = postRepository.findStoryListWithPage(searchCondition, 1, 10);


        assertThat(stories.size()).isEqualTo(1);
        assertThat(stories.get(0).getTitle()).isEqualTo("제목입니다22");
    }

    @Test
    void post_페이징(){
        List<StoryListResponseDto> storyListWithPage = postRepository.findStoryListWithPage(new SearchCondition(), 4, PAGE_SIZE);

        assertThat(storyListWithPage.size()).isEqualTo(3);
    }

    @Test
    void 단건조회(){
        Post post = postRepository.findAll().get(0);
        Post singlePost = postRepository.findSinglePost(post.getId());
        assertThat(singlePost.getPhotos().size()).isEqualTo(3);
    }

    @Test
    void 일정확인(){
        Post post = postRepository.findAll().get(0);
        Post singlePost = postRepository.findSinglePost(post.getId());
        LocalDate startDate = singlePost.getPeriod().getStartDate();
        LocalDate endDate = singlePost.getPeriod().getEndDate();

        assertThat(startDate).isEqualTo(LocalDate.of(2021, 11, 2));
        assertThat(endDate).isEqualTo(LocalDate.of(2021, 11, 5));
    }

    @Test
    void post_개수(){
        long count = postRepository.getPostTotalCount(new SearchCondition());
        assertThat(count).isEqualTo(30);
    }

    @Test
    void post_개수_지역검색(){
        SearchCondition condition = new SearchCondition();
        condition.setRegion("부산");
        long count = postRepository.getPostTotalCount(condition);
        assertThat(count).isEqualTo(14);
    }

}