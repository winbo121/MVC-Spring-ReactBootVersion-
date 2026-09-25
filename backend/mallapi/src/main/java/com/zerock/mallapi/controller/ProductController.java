package com.zerock.mallapi.controller;

import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.ProductDTO;
import com.zerock.mallapi.service.ProductService;
import com.zerock.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil customFileUtil;

    private final ProductService productService;

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO){
        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/register")
    public Map<String,String> register(ProductDTO productDTO) throws IOException {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = customFileUtil.saveFiles(files);

        // 최종 이름들을 이제 이거로 DB에 넘기기
        productDTO.setUploadFileNames(uploadFileNames);

        productService.regster(productDTO);

        return Map.of("Result","Success");
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno){
        return productService.getOne(pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) throws IOException {
        return customFileUtil.getFile(fileName);
    }

    // 상품 수정
    // 프론트에서 넘어오는 것:
    //  - uploadFileNames: 화면에서 DELETE 안 하고 남긴 기존 파일 이름
    //  - files: 새로 업로드할 파일
    @PutMapping("/{pno}")
    public Map<String,String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) throws IOException {

        // 수정 전 원본 (나중에 화면에서 뺀 파일만 디스크에서 지우기 위해)
        ProductDTO oldProductDTO = productService.getOne(pno);

        // 새로 올린 파일을 저장
        List<MultipartFile> files = productDTO.getFiles();


        if (files != null) {
            files.forEach(file ->
                    log.info("getFiles() originalFilename = {}, empty = {}",
                            file.getOriginalFilename(), file.isEmpty())
            );
        }

        List<String> currentUploadFileNames = customFileUtil.saveFiles(files);

        // 화면에서 유지한 기존 파일 이름
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        log.info("getUploadFileNames() = {}", uploadedFileNames);

        if (uploadedFileNames == null) {
            uploadedFileNames = new ArrayList<>();
            productDTO.setUploadFileNames(uploadedFileNames);
        }

        // 최종 파일 목록 = 남긴 기존 파일 + 새로 저장한 파일
        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        // DB의 상품 정보/이미지 목록을 최종 파일 목록으로 교체
        productService.modify(productDTO);

        // 원래 있던 파일 중 최종 목록에 없는 것만 실제 파일 삭제
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> finalUploadedFileNames = uploadedFileNames;
            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> finalUploadedFileNames.indexOf(fileName) == -1)
                    .toList();
            customFileUtil.deleteFiles(removeFiles);
        }

        return Map.of("Result","Success");
    }

    @DeleteMapping("/{pno}")
    public Map<String,String> delete(@PathVariable("pno") Long pno){

        //삭제할 저장되어있는 파일 이름들을 가져오기위한 작업
        ProductDTO oldProductDTO = productService.getOne(pno);

        //실제 파일삭제
        customFileUtil.deleteFiles(oldProductDTO.getUploadFileNames());

        productService.remove(pno);

        return Map.of("Result","Success");
    }
}


