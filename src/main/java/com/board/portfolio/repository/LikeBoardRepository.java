package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.domain.entity.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeBoardRepository extends JpaRepository<LikeBoard,String> {
    Optional<LikeBoard> findByBoardAndAccount(Board board, Account account);
}
