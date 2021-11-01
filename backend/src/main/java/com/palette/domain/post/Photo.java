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

    private String uploadFileName; // 실제 업로드 이름

    private String storeFileName; // 저장될 파일 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_photo_post"))
    private Post post;

    public Photo(String uploadFileName, String storeFileName, Post post) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.post = post;
    }
}