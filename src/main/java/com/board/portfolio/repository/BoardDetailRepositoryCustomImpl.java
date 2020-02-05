package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.domain.entity.BoardDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.board.portfolio.domain.entity.QBoardDetail.boardDetail;

@RequiredArgsConstructor
public class BoardDetailRepositoryCustomImpl implements BoardDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardDetail> getBoardList(int size) {
        return queryFactory
                .selectFrom(boardDetail)
                .orderBy(boardDetail.regDate.desc())
                .limit(size)
                .fetch();
    }
}
