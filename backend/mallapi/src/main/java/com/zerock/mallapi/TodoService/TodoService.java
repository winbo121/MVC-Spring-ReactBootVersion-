package com.zerock.mallapi.TodoService;

import com.zerock.mallapi.dto.TodoDTO;

public interface TodoService {

    Long register(TodoDTO todoDTO);

    TodoDTO get(Long tno);
}
