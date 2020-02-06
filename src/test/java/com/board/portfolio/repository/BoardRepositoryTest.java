package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.paging.BoardPagination;
import com.board.portfolio.paging.PageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;



@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    BoardPagination boardPagination;
    @Autowired
    BoardRepository boardRepository;
    @Test
    public void getBoardLimitTest(){
        List boardList = boardRepository.getBoardList(0,1);
    }
    @Test
    public void getPaginationTest(){
        PageDTO<Board> page = boardPagination.getPaginationList(1);
    }

    @Test
    public void l2CacheTest(){
//        Board board = new Board();
//        board.setBoardId((long)32);
//        System.out.println("----------32-------");
//        boardRepository.findById(board.getBoardId());
//        int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
//                .getCache("board").getSize();
//        System.out.println(size);
//        boardRepository.findById(board.getBoardId());
//        System.out.println("----------33-------");
//        boardRepository.findById((long)33);
//        int size2 = CacheManager.ALL_CACHE_MANAGERS.get(0)
//                .getCache("board").getSize();
//        System.out.println(size2);
//        boardRepository.findById((long)33);
//        System.out.println("------");
    }

}