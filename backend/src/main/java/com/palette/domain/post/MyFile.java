package com.palette.domain.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Table(name = "file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MyFile {

    private String uploadFileName; // 실제 파일명
    private String storeFileName; // 저장된 파일명

    public MyFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
