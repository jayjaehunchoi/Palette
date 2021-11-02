package com.palette.domain.post;

import com.palette.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Photo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @Embedded
    private MyFile file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_photo_post"))
    private Post post;

    public Photo(MyFile file, Post post) {
        this.file = file;
        this.post = post;
    }
}