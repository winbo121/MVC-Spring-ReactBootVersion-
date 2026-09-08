package com.zerock.mallapi.repository;

import com.zerock.mallapi.domain.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testInsert(){

        for(int i=0; i<10; i++){

            Product product = Product.builder()
                    .pname("Test"+i)
                    .pdcsc("testDesc"+i+" desc")
                    .build();

            product.addImageString(UUID.randomUUID()+"_"+"test"+i);
            product.addImageString(UUID.randomUUID()+"_"+"test"+i);

            productRepository.save(product);
        }
    }

    @Transactional
    @Test
    public void testRead(){
        Long pno= 1L;

        Optional<Product> result = productRepository.selectOne(pno);

        Product product =result.orElseThrow();

        log.info("product -> "+product);
        log.info("productImageList -> "+product.getImageList());

    }

    @Commit
    @Transactional
    @Test
    public void testDelete(){

        Long pno = 2L;

        productRepository.updateToDelete(pno,true);
    }

    @Test
    public void testUpdate(){

        Long pno = 1L;

        Product product = productRepository.selectOne(pno).get();

        //첨부파일 수정
        product.clearImageList(); //딜레트문 작동

        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE2.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE3.jpg");

        productRepository.save(product);
    }



    @Test
    public void testList(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());

        Page<Object[]> result  = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }
}
