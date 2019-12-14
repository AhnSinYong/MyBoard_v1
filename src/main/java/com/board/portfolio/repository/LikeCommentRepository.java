package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.Comment;
import com.board.portfolio.domain.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment,String> {

    Optional<LikeComment> findByCommentAndAccount(Comment comment, Account account);
}
