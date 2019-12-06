package com.board.portfolio.service;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.paging.BoardPagination;
import com.board.portfolio.paging.PageDTO;
import com.board.portfolio.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private BoardPagination boardPagination;

    @Autowired
    public BoardService(BoardRepository boardRepository,
                        BoardPagination boardPagination){
        this.boardRepository = boardRepository;
        this.boardPagination = boardPagination;
    }

    public PageDTO<Board> getPaginBoardList(int page){
        return boardPagination.getPaginationList(page);
    }
}
