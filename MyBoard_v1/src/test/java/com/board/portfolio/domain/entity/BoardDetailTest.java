package com.board.portfolio.domain.entity;

import com.board.portfolio.repository.BoardDetailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardDetailTest {

    @Autowired
    BoardDetailRepository boardDetailRepository;
    @Test
    public void boardDetailTest(){
        boardDetailRepository.findAll();
    }

}