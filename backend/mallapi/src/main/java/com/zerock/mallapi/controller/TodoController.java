package com.zerock.mallapi.controller;

import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.TodoDTO;
import com.zerock.mallapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable(name="tno") Long tno){
        return todoService.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        return todoService.list(pageRequestDTO);
    }

    @PostMapping("/regist")
    public Map<String,String> regist (@RequestBody TodoDTO todoDTO){

        /*
         -- localhost:8080/api/todo/regist  포스트로 JSON 형식으로 요청
        {
            "title" : "test55804",
            "writer" : "ghostwriter",
            "dueDate" : "2024-03-05"
        }
        */

        todoService.register(todoDTO);

        return Map.of("Result","Success");
    }
}
