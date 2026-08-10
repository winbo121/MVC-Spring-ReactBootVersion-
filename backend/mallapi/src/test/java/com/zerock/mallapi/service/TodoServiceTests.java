package com.zerock.mallapi.service;

import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
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

    @Test
    public void testList(){

        //빌더에 SIZE는 안하는이유는 이미 디폴트 빌더를 SIZE에 @Builder.Default 10으로 했기 때문입니다.
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(2).build();

        PageResponseDTO<TodoDTO> pageResponseDTO = todoService.list(pageRequestDTO);

        log.info("pageResponseDTO ->"+pageResponseDTO);

        /* 리턴값
        PageResponseDTO(
            dtoList=[
                TodoDTO(title=service Test, writer=tester, dueDate=2022-04-22),
                TodoDTO(title=Title...100, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...99, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...98, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...97, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...96, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...95, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...94, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...93, writer=user00, dueDate=2023-12-31),
                TodoDTO(title=Title...92, writer=user00, dueDate=2023-12-31)
            ],

            pageNumList=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10],

            pageRequestDTO=PageRequestDTO(page=2, size=10),

            prev=false,
            next=true,
            totalCount=102,
            prevPage=0,
            nextPage=11,
            totalPage=10,
            current=2
        )
        */

    }
}
