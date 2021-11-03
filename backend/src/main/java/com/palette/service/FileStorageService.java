package com.palette.service;


import com.palette.domain.post.MyFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.dir}")
    private String fileDir;

    // todo : aws s3 연동 필요
    // 파일 저장 디렉터리 full path
    public String getFullPath(String fileName){
        return fileDir + fileName;
    }

    public MyFile storeFile(MultipartFile multipartFile) throws IOException{
        log.info("파일 저장 시작");
        if(multipartFile.isEmpty()){
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new MyFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFileName){
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid+"."+ext;
    }

    private String extractExt(String originalFileName){
        int pos = originalFileName.lastIndexOf('.');
        return originalFileName.substring(pos+1);
    }
}
