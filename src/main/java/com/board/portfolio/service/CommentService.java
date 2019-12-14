package com.board.portfolio.service;

import com.board.portfolio.domain.dto.CommentDTO;
import com.board.portfolio.domain.entity.*;
import com.board.portfolio.exception.NotAllowAccessException;
import com.board.portfolio.exception.NotFoundCommentException;
import com.board.portfolio.repository.CommentRepository;
import com.board.portfolio.repository.LikeCommentRepository;
import com.board.portfolio.security.account.AccountSecurityDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private LikeCommentRepository likeCommentRepository;
    private ModelMapper modelMapper;
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          LikeCommentRepository likeCommentRepository,
                          ModelMapper modelMapper){
        this.commentRepository = commentRepository;
        this.likeCommentRepository = likeCommentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Map getCommentList(Long boardId,AccountSecurityDTO accountDTO) {
        List<Comment> commentList = commentRepository.findAllByBoardOrderByGroupAscRegDateAsc(new Board(boardId));
        Map data = new HashMap();
        data.put("commentList", commentList);
        List isLikedList = new ArrayList();

        String email = accountDTO.getEmail();
        if(commentList!=null){

            for(Comment comment: commentList){
                boolean isLiked  = isLikedComment(comment.getLikeCommentList(), email);
                isLikedList.add(isLiked);
            }
        }
        data.put("isLikedList",isLikedList);
        return data;
    }
    private boolean isLikedComment(List<LikeComment> likeCommentList, String email){
        if(email==null){
            return false;
        }
        for(LikeComment likeComment : likeCommentList){
            if(likeComment.getAccount().getEmail().equals(email)){
                return true;
            }
        }
        return false;
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

        Optional<Comment> opChildCommnet =commentRepository.findTopByBoardAndGroupAndCommentIdGreaterThanOrderByRegDateAsc(comment.getBoard(),comment.getGroup(),commentId);
        if(opChildCommnet.isPresent()){
            Comment childComment = opChildCommnet.get();
            childComment.increaseDelParentCnt(comment.getDelParentCnt()+1);
            childComment.setHasDelTypeParent(comment.isHasDelTypeParent());
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
                .hasDelTypeParent(true)
                .build();
        comment = commentRepository.save(comment);
        comment.setGroup(comment.getCommentId());

        Map data = new HashMap();
        data.put("boardId", comment.getBoard().getBoardId());
        return data;
    }

    @Transactional
    public Map likeComment(Long commentId, AccountSecurityDTO accountDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundCommentException("comment isn't exist"));
        Account account = modelMapper.map(accountDTO, Account.class);

        Optional<LikeComment> opLikeComment = likeCommentRepository.findByCommentAndAccount(comment,account);
        if(opLikeComment.isPresent()){
            likeCommentRepository.delete(opLikeComment.get());
            comment.decreaseLike();
        }
        else{
            likeCommentRepository.save(new LikeComment(comment, account));
            comment.increaseLike();
        }

        Map data = new HashMap();
        data.put("like",comment.getLike());
        return data;
    }

    @Transactional
    public Map replyWriteComment(CommentDTO.Reply dto, AccountSecurityDTO accountDTO) {
        Comment parentComment = commentRepository.findById(dto.getCommentId()).orElseThrow(()->new NotFoundCommentException("comment isn't exist"));
        Comment comment = Comment.builder()
                .board(new Board(dto.getBoardId()))
                .content(dto.getContent())
                .account(new Account(accountDTO.getEmail()))
                .type(CommentType.CHILD)
                .group(parentComment.getGroup())
                .hasDelTypeParent(false)
                .build();
        comment = commentRepository.save(comment);

        Map data = new HashMap();
        data.put("boardId",dto.getBoardId());
        return data;
    }
}
