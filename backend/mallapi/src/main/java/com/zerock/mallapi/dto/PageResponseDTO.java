package com.zerock.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO , long totalCount){

        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int)totalCount;

        int end = (int)(Math.ceil(pageRequestDTO.getPage()/10.0))*10; //ex) 3페이지면 end 값이 10 , 14페이지면 end 값이 20
        //  3 / 10.0 -> 0.3 -> Math.ceil(0.3) -> 1 -> 1*10 -> 10
        //  7 / 10.0 -> 0.7 -> Math.ceil(0.7) -> 1 -> 1*10 -> 10
        //  14 / 10.0 -> 1.4 -> Math.ceil(1.4) -> 2 -> 2*10 -> 20
        //  20 / 10.0 -> 2 -> Math.ceil(2) -> 2 -> 2*10 -> 20

        int start = end -9;

        int last = (int)(Math.ceil(totalCount/(double)pageRequestDTO.getSize()));
        // 38 / 10.0 -> 3.8 -> Math.ceil(3.8) -> 4
        // 71 / 10.0 -> 7.1 -> Math.ceil(7.1) -> 8
        // 145 / 10.0 -> 14.5 -> Math.ceil(14.5) -> 15
        // 200 / 10.0 -> 20.0 -> Math.ceil(20.0) -> 20

        end = last > end ? end: last;

        this.prev = start > 1 ;
        this.next = totalCount > end * pageRequestDTO.getSize();

        this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

        if(prev){
            this.prevPage = start - 1;
        }

        if(next){
            this.nextPage = end + 1;
        }

        this.totalPage = this.pageNumList.size();
        this.current = pageRequestDTO.getPage();
    }
}
