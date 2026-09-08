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

    @PutMapping("/{pno}")
    public Map<String,String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) throws IOException {

        //삭제할 저장되어있는 파일 이름들을 가져오기위한 작업
        ProductDTO oldProductDTO = productService.getOne(pno);

        //실제 파일삭제
        customFileUtil.deleteFiles(oldProductDTO.getUploadFileNames());

        //파라미터로 날라온 실제 파일들 가져오기
        List<MultipartFile> files = productDTO.getFiles();

        //파라미터로 날라온 실제 파일들 저장하기
        List<String> uploadFileNames = customFileUtil.saveFiles(files);

        // 최종 이름들을 이제 이거로 DB에 넘기기
        productDTO.setUploadFileNames(uploadFileNames);

        productService.modify(productDTO);

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


