package com.zerock.mallapi.service;

import com.zerock.mallapi.TodoService.TodoService;
import com.zerock.mallapi.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister(){

        TodoDTO todoDTO = TodoDTO.builder()
                .title("service Test")
                .writer("tester")
                .dueDate(LocalDate.of(2022,4,22))
                .build();

        //여기 안에서 DTO값들을 Entity에 적용하는 과정
        Long tno =todoService.register(todoDTO);

        log.info("새로 들어간 tno 값 ->" +tno);
    }

    @Test
    public void testGet(){

        Long tno = 10L;

        //여기 안에서 DB에서 받아온 Entity값들을 DTO에 적용하는 과정
        TodoDTO todoDTO = todoService.get(tno);

        //todoDTO -->TodoDTO(title=Title...9, writer=user00, dueDate=2023-12-31) 즉 dto로 마지막에 내보내기 때문에 tno값을 조회할수 없다.
        log.info("todoDTO -->"+todoDTO);
    }
}
