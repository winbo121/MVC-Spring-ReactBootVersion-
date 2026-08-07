package com.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity //디비영역(테이블과 동일)
@Table (name = "tbl_todo")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String title;
    private String writer;
    private boolean complete;
    private LocalDate dueDate;

    /*

    <Entity , DTO 차이점>

    브라우저 -> Controller -> MemberSaveDto -> Service -> Member(Entity) -> Repository -> DB
    DB -> Repository -> Member(Entity) -> Service -> Controller -> MemberSaveDto(변환 리턴)

    ex)
    ├── controller
    │      MemberController
    │
    ├── service
    │      MemberService
    │
    ├── repository
    │      MemberRepository
    │
    ├── entity
    │      Member.java
    │
    └── dto
           MemberSaveDto.java
           MemberUpdateDto.java
           MemberResponseDto.java
           MemberLoginDto.java

    --------------------------------------------------------------

    @GetMapping
    public MemberResponseDto find() {

        Member member = memberRepository.findById(1L).get();

        //회원 넘버 제외 (보안문제로)
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

  */

}
