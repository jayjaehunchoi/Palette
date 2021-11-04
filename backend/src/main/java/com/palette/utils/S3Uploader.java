package com.palette.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.palette.domain.post.MyFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private static String DIR_NAME = "static";

    private String localFileDir = System.getProperty("user.dir"); //현재 실행 위치

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public List<MyFile> uploadFiles(List<MultipartFile> multipartFiles) throws IOException{
        List<MyFile> myFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));
            myFiles.add(uploadToS3(uploadFile));
        }
        return myFiles;
    }

    public void deleteS3(List<String> storedFileNames){
        storedFileNames.forEach(file -> amazonS3Client.deleteObject(bucket,file));
    }

    private MyFile uploadToS3(File uploadFile){
        String originalName = uploadFile.getName();
        String fileName = DIR_NAME+"/"+ UUID.randomUUID()+originalName;
        String storeFileName = putS3(uploadFile, fileName);
        removeLocalFile(uploadFile);
        return new MyFile(originalName, storeFileName);
    }

    // s3에 지정 경로에 (이미지)객체 삽입
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // s3에 파일 저장되었으면 로컬 이미지 제거
    private void removeLocalFile(File file){
        if(file.delete()){
            log.info("로컬 파일 제거 성공");
            return;
        }
        log.info("로컬 파일 제거 실패");
    }

    // 전달된 multipartfile을 현재 작업 폴더에 file로 전환
    private Optional<File> convert(MultipartFile multipartFile)throws IOException{
        File convertFile = new File(localFileDir + "/" + multipartFile.getOriginalFilename());
        if(convertFile.createNewFile()){
            try (FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }



}
