package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.paging.BoardPagination;
import com.board.portfolio.paging.PageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    BoardPagination boardPagination;
    @Autowired
    BoardRepository boardRepository;
    @Test
    public void getBoardLimitTest(){
        List boardList = boardRepository.findLimitByBoard(0,1);
    }
    @Test
    public void getPaginationTest(){
        PageDTO<Board> page = boardPagination.getPaginationList(1);
    }

}