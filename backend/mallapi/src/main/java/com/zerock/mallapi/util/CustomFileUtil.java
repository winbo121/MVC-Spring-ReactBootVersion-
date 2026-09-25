package com.zerock.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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

            if(file == null || file.isEmpty()){
                continue;
            }

            String saveName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();

            //실제 파일저장할 path지정
            Path savePath = Paths.get(uploadPath,saveName);

            //실제 파일 저장
            Files.copy(file.getInputStream(),savePath);

            String contentType = file.getContentType(); //이미지 , 엑셀 , 한글 등등 파일 타입 반환

            //이미지일 경우 썸네일 지정 즉 이미지 크기 고정
            if(contentType != null && contentType.startsWith("image") ){

                //이미지 일때는 썸내일로 뱉어야하기때문에 이렇게 조정
                saveName="fix_"+saveName;

                Path imagePath = Paths.get(uploadPath,saveName);

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

    public ResponseEntity<Resource> getFile(String fileName) throws IOException {

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

        if(!resource.isReadable()){
            resource = new FileSystemResource(uploadPath+File.separator+"default.jpg");
        }

        if(!resource.isReadable()){
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String contentType = Files.probeContentType(resource.getFile().toPath());
        if(contentType != null){
            headers.add("Content-Type", contentType);
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames) {

        if(fileNames == null || fileNames.size()==0){
            return;
        }

        fileNames.forEach(fileName -> {
            Path filePath = Paths.get(uploadPath,fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
