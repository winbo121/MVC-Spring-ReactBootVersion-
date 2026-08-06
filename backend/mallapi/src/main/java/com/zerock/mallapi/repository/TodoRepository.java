package com.zerock.mallapi.repository;

import com.zerock.mallapi.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    //제목으로 조회가 가능한 페이징처리 리스트 조회용 (직접 만듬)
    Page<Todo> findByTitleContaining(String title, Pageable pageable);
}
