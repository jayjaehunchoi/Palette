package com.palette.domain.post;

import com.palette.domain.BaseTimeEntity;
import com.palette.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String comment_content;

    private Long parent_comment_id;

    public Comment(Post post, Member member, String comment_content, Long parent_comment_id) {
        this.post = post;
        this.member = member;
        this.comment_content = comment_content;
        this.parent_comment_id = parent_comment_id;
    }
}