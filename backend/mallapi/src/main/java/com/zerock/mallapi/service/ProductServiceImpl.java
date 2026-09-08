package com.zerock.mallapi.service;

import com.zerock.mallapi.domain.Product;
import com.zerock.mallapi.domain.ProductImage;
import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.ProductDTO;
import com.zerock.mallapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements  ProductService{

    private final ProductRepository productRepository;

    //DTO -> ENTITY 전환용
    private Product dtoToEntity(ProductDTO productDTO){
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdcsc(productDTO.getPdcsc())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames == null || uploadFileNames.size() ==0){
            return product;
        }

        uploadFileNames.forEach(fileName ->{
            product.addImageString(fileName);
        });

        return product;
    }

    //ENTITY -> DTO 전환용
    private ProductDTO entityToDTO(Product product){

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdcsc(product.getPdcsc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList.isEmpty()){
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage ->
           productImage.getFileName()).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
    }

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize(), Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDTO> dtoList =result.get().map(arr -> {
            ProductDTO productDTO = null;

            Product product = (Product) arr[0];

            log.info("arr ->"+ Arrays.toString(arr));

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdcsc(product.getPdcsc())
                    .price(product.getPrice())
                    .build();

            ProductImage productImage = (ProductImage) arr[1];



            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return  productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();


        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public void regster(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO);

        productRepository.save(product);
    }

    @Override
    public ProductDTO getOne(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        return entityToDTO(product);
    }

    @Override
    public void modify(ProductDTO productDTO) {

        //조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        product.setPrice(productDTO.getPrice());
        product.setPname(productDTO.getPname());
        product.setPdcsc(productDTO.getPdcsc());
        product.setDelFlag(productDTO.isDelFlag());

        //이미지처리
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(!uploadFileNames.isEmpty()){
            product.clearImageList();

            uploadFileNames.forEach(uploadName ->{
                product.addImageString(uploadName);
            });

        }

        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);
    }

}
