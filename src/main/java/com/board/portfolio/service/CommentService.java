package com.board.portfolio.service;

import com.board.portfolio.domain.dto.CommentDTO;
import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.domain.entity.Comment;
import com.board.portfolio.domain.entity.CommentType;
import com.board.portfolio.exception.NotAllowAccessException;
import com.board.portfolio.exception.NotFoundCommentException;
import com.board.portfolio.repository.CommentRepository;
import com.board.portfolio.security.account.AccountSecurityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Map getCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findAllByBoardOrderByGroupAscRegDateAsc(new Board(boardId));
        Map data = new HashMap();
        data.put("commentList", commentList);
        return data;
    }


    @Transactional
    public Map modifyComment(Long commentId, CommentDTO.Modify dto, AccountSecurityDTO accountDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundCommentException("comment isn't exist"));
        if(!comment.getAccount().getEmail().equals(accountDTO.getEmail())){
            throw new NotAllowAccessException("not allow access");
        }
        comment.setContent(dto.getContent());
        comment.setUpDate(new Date());

        Map data = new HashMap();
        data.put("boardId", comment.getBoard().getBoardId());
        return data;
    }

    @Transactional
    public Map deleteComment(Long commentId, AccountSecurityDTO accountDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundCommentException("comment isn't exist"));
        if(!comment.getAccount().getEmail().equals(accountDTO.getEmail())){
            throw new NotAllowAccessException("not allow access");
        }
        commentRepository.delete(comment);

        Map data = new HashMap();
        data.put("boardId", comment.getBoard().getBoardId());
        return data;
    }

    @Transactional
    public Map writeComment(Long boardId, CommentDTO.Write dto, AccountSecurityDTO accountDTO) {
        Comment comment = Comment.builder()
                .board(new Board(boardId))
                .content(dto.getContent())
                .account(new Account(accountDTO.getEmail()))
                .type(CommentType.PARENT)
                .group((long)-1)
                .build();
        comment = commentRepository.save(comment);
        comment.setGroup(comment.getCommentId());

        Map data = new HashMap();
        data.put("boardId", comment.getBoard().getBoardId());
        return data;
    }
}
