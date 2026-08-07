package com.zerock.mallapi.TodoService;

import com.zerock.mallapi.domain.Todo;
import com.zerock.mallapi.dto.TodoDTO;
import com.zerock.mallapi.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class TodoServiceImpl implements TodoService{

    //TodoRepository
    private final TodoRepository todoRepository;

    //ModelMapper
    private final ModelMapper modelMapper;

    @Override
    public Long register(TodoDTO todoDTO) {

        //DTO로 받아온것을 Entity로 전환 즉 Entity(DB직속)에 집어넣기
        Todo todo = modelMapper.map(todoDTO, Todo.class);

        Todo savedTodo = todoRepository.save(todo);

        //새로 저장된 todo에값을 리턴
        return savedTodo.getTno();
    }

    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        //DB에서 받아온 Entity를 DTO로 전환 즉 DTO에 집어넣기
        TodoDTO todoDTO = modelMapper.map(todo, TodoDTO.class);

        return todoDTO;
    }

}
