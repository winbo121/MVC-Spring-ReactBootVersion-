package com.zerock.mallapi.service;

import com.zerock.mallapi.domain.Todo;
import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.TodoDTO;
import com.zerock.mallapi.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(),pageRequestDTO.getSize(), Sort.by("tno").descending());

        // 리스트들을 엔터티로 받기
        Page<Todo> result = todoRepository.findAll(pageable);

        // 엔터티로 받은것을 리스트 DTO로 변경
        List<TodoDTO> dtoList =  result.getContent().stream().map(
                todoEntity -> modelMapper.map(todoEntity,TodoDTO.class))
                .collect(Collectors.toList());

        //전체 카운트 (리미트 안걸려있는)
        long totalCount = result.getTotalElements();

        //페이지 리퀘스트 VO와 토탈 카운트 그리고 조회된(리미트 걸려있는)리스트를 파라미터로 넘긴다. (페이지 조작시작)
        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();

        // 여기에는 일반 리스타와 페이지 관련된 데이터 모든것들을 가지고 리턴한다.
        return responseDTO;
    }

    @Override
    public void update(TodoDTO todoDTO) {
        //DTO로 받아온것을 Entity로 전환 즉 Entity(DB직속)에 집어넣기
        Todo todo = modelMapper.map(todoDTO, Todo.class);

        todoRepository.save(todo);
    }

    @Override
    public void delete(Long tno) {
        todoRepository.deleteById(tno);
    }

}
