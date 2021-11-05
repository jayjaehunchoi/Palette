package com.palette.domain.post;

import com.palette.domain.BaseTimeEntity;
import com.palette.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = {"comment_id", "comment_content"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_comment_post"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_comment_member"))
    private Member member;

    private String commentContent;

    private Long parentCommentId;

    @Builder
    public Comment(Member member, String commentContent) {
        this.member = member;
        this.commentContent = commentContent;
    }

    // 답글이 아니라면 parent 기본값 0으로 줄 것
    public void writeComment(Post post, Long parentCommentId){
        this.post = post;
        this.parentCommentId = parentCommentId;
        post.getComments().add(this);
    }

    public Comment updateComment(String commentContent){
        this.commentContent = commentContent;
        return this;
    }

    public void removeComment(Comment comment){
        post.getComments().remove(comment);
    }
}