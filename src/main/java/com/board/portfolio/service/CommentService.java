package com.board.portfolio.service;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.domain.entity.Comment;
import com.board.portfolio.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Map getCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findAllByBoardOrderByGroupAscRegDateAsc(new Board(boardId));
        Map data = new HashMap();
        data.put("commentList", commentList);
        return data;
    }
}
