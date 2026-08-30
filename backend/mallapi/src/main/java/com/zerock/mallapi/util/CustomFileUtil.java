package com.zerock.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init(){
        File tempFolder = new File(uploadPath);

        if(!tempFolder.exists()){
            tempFolder.mkdir();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("파일 업로드 경로---"+uploadPath);

    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException, IOException {

        if(files == null || files.size() ==0){
            return List.of();
        }

        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file: files){

            String saveName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();

            //실제 파일저장할 path지정
            Path savePath = Paths.get(uploadPath,saveName);

            //실제 파일 저장
            Files.copy(file.getInputStream(),savePath);

            String contentType = file.getContentType(); //이미지 , 엑셀 , 한글 등등 파일 타입 반환

            //이미지일 경우 썸네일 지정 즉 이미지 크기 고정
            if(contentType != null && contentType.startsWith("image") ){
                Path imagePath = Paths.get(uploadPath,"fix_"+saveName);

                //새로 고쳐진 이미지 업로드 추가
                Thumbnails.of(savePath.toFile())
                        .size(200,200)
                        .toFile(imagePath.toFile());
            }

            //반환할 uuid로 변환된 파일 이름들 리스트 (DB저장용)
            uploadNames.add(saveName);
        }

        return uploadNames;
    }
}
