package com.palette.domain.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UploadFile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "upload_files_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    private String uploadFileName; // 실제 업로드 이름

    private String storeFileName; // 저장될 파일 이름



}