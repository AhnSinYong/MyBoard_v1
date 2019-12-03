package com.board.portfolio.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> list;
    private long page;
    private long startPage;
    private long endPage;
    private long prevPage;
    private long nextPage;
}
