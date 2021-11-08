package com.palette.domain.post;

import com.palette.domain.BaseTimeEntity;
import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.dto.request.PostRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_post_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_group_id", foreignKey = @ForeignKey(name = "fk_post_post_group"))
    private PostGroup postGroup;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Embedded
    private Period period;

    // 지역
    private String region;

    private int likeCount;

    // 조회수
    private int hit;

    @Builder
    public Post(String title, String content, Member member, Period period, String region) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.period = period;
        this.region = region;
        this.likeCount = 0;
        this.hit = 0;
    }

    public void update(PostRequestDto dto){
        title = dto.getTitle();
        content = dto.getContent();
    }

    public void visitPost(){
        hit++;
    }

    public void pushLike(int num){
        likeCount += num;
    }

    public void createPostOnPostGroup(PostGroup postGroup){
        this.postGroup = postGroup;
    }
}
