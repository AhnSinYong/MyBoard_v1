package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query(value = "SELECT * " +
                    "FROM TB_BOARD " +
                    "ORDER BY REG_DATE DESC " +
                    "LIMIT :start, :size ",
            nativeQuery = true)
    List<Board> findLimitByBoard(@Param("start")long start, @Param("size")int size);
}
