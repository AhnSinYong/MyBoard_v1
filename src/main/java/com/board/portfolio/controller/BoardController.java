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
                                    @ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        boardService.writePost(dto, accountDTO);
        return ResponseEntity.ok(Result.SUCCESS);
    }
    @GetMapping("/board/post/{boardId}")
    public ResponseEntity readPost(@PathVariable long boardId,
                                   @ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        return ResponseEntity.ok(boardService.readPost(boardId,accountDTO));
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/board/like")
    public ResponseEntity likePost(@RequestBody @Valid BoardDTO.Like dto,
                                   @ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        return ResponseEntity.ok(boardService.likePost(dto,accountDTO));
    }


    @ModelAttribute("accountDTO")
    private AccountSecurityDTO getAccountDTO(Authentication authentication){
        if(authentication.getPrincipal().equals("")){
            return new AccountSecurityDTO();
        }
        return (AccountSecurityDTO)authentication.getPrincipal();
    }
}
