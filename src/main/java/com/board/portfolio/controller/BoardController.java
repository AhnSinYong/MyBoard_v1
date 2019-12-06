package com.board.portfolio.controller;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity writePost(@ModelAttribute BoardDTO.Write dto,
                                    @ModelAttribute("fileList") List<MultipartFile> fileList){
        return ResponseEntity.ok(Result.SUCCESS);
    }
}
