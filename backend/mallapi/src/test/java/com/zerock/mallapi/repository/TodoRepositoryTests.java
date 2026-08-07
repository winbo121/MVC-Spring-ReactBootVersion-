package com.zerock.mallapi.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerock.mallapi.domain.QTodo;
import com.zerock.mallapi.domain.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    public TodoRepository todoRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    public void testInsert(){

        for (int i =0 ; i<=100; i++){

            Todo todo = Todo.builder()
                    .title("Title..."+i)
                    .dueDate(LocalDate.of(2023,12,31))
                    .writer("user00")
                    .build();

            todoRepository.save(todo);
        }
    }

    @Test
    public void testRead(){

        Long tno = 33L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        log.info("todo --> "+todo);
    }

    @Test
    public void testModify(){

        Long tno = 33L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        //빌더는 새로 생성해서 넣는것이고 setter는 있는것을 토스에서 그자리에서 새로 갱신하는것이다.
        todo.setTitle("Modify..."+32);
        todo.setDueDate(LocalDate.of(2025,12,31));
        todo.setWriter("user01");

        todoRepository.save(todo);
    }

    @Test
    public void testPaging(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        result.getContent().stream().forEach(todo ->
                    log.info("todoList -->" + todo)
                );

        Page<Todo> result1 = todoRepository.findByTitleContaining("modify", pageable);

        result1.getContent().stream().forEach(todo ->
                    log.info("todoList(Search Title) -->" + todo)
                );

    }

    //jpaQueryFactory 이용해서 title로 '11' 글자 데이터 검색
    @Test
    public void testPagingQureryds(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("tno").descending());

        QTodo qTodo = QTodo.todo;

        List<Todo> list =jpaQueryFactory.selectFrom(qTodo).where(qTodo.title.contains("11")).fetch();

        log.info("list ->"+list);
    }

}
