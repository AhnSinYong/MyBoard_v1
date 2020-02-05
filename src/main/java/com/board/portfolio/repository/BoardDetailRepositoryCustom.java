package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.BoardDetail;

import java.util.List;

public interface BoardDetailRepositoryCustom {
    List<BoardDetail> getBoardList(int size);
}
