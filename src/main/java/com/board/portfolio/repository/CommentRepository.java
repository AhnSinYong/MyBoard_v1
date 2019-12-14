package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByBoardOrderByGroupAscRegDateAsc(Board board);

    Optional<Comment> findTopByBoardAndGroupAndCommentIdGreaterThanOrderByRegDateAsc(Board board, Long group, Long commentId);
}
