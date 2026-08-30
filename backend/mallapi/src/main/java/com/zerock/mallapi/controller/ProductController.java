package com.zerock.mallapi.controller;

import com.zerock.mallapi.dto.ProductDTO;
import com.zerock.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil customFileUtil;

    @PostMapping("/")
    public Map<String,String> register(ProductDTO productDTO) throws IOException {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = customFileUtil.saveFiles(files);

        // 최종 이름들을 이제 이거로 DB에 넘기기
        productDTO.setUploadFileNames(uploadFileNames);

        return Map.of("Result","Success");
    }
}
