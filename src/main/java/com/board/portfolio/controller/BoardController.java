package com.board.portfolio.controller;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.security.account.AccountSecurityDTO;
import com.board.portfolio.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/board")
    public ResponseEntity writePost(@Valid BoardDTO.Write dto,
                                    Authentication authentication){
        boardService.writePost(dto, castAccountDTO(authentication));
        return ResponseEntity.ok(Result.SUCCESS);
    }
    @GetMapping("/board/post/{boardId}")
    public ResponseEntity readPost(@PathVariable long boardId){
        return ResponseEntity.ok(boardService.readPost(boardId));
    }

    private AccountSecurityDTO castAccountDTO(Authentication authentication){
        return (AccountSecurityDTO)authentication.getPrincipal();
    }
}
