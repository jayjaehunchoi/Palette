package com.palette.domain.post;

import com.palette.domain.BaseTimeEntity;
import com.palette.domain.member.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(of = "likes_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "likes")
@Entity
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_likes_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_likes_post"))
    private Post post;

    public Like(Member member) {
        this.member = member;
    }
    public void pushLike(Post post, boolean isExist){
        if(isExist){
            post.getLikes().remove(this);
            post.pushLike(-1);
            return;
        }
        this.post = post;
        post.getLikes().add(this);
        post.pushLike(1);
    }

}