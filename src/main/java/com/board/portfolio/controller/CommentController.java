package com.board.portfolio.controller;

import com.board.portfolio.security.account.AccountSecurityDTO;
import com.board.portfolio.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/comment/{boardId}")
    public ResponseEntity getCommentList(@PathVariable Long boardId,
                                         @ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        return ResponseEntity.ok(commentService.getCommentList(boardId));
    }


}
