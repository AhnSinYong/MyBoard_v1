package com.board.portfolio.controller;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class BoardController {

    BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }
    @GetMapping("/board/{page}")
    public ResponseEntity getBoardList(@PathVariable int page){
        return ResponseEntity.ok(boardService.getPaginBoardList(page));
    }
    @PostMapping("/board")
    public ResponseEntity writePost(@Valid BoardDTO.Write dto){
        return ResponseEntity.ok(Result.SUCCESS);
    }
}
