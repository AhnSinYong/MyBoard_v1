package com.board.portfolio.paging;

import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.repository.BoardRepository;
import com.board.portfolio.store.repository.StoredBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BoardPagination extends Pagination {

    private StoredBoardRepository storedBoardRepository;
    private BoardRepository boardRepository;
    @Autowired
    public BoardPagination(@Value("${board.pageSize}") int pageSize,
                           @Value("${board.rangeSize}") int rangeSize,
                           BoardRepository boardRepository,
                           StoredBoardRepository storedBoardRepository){
        super(pageSize, rangeSize);
        this.boardRepository = boardRepository;
        this.storedBoardRepository = storedBoardRepository;
    }

    @Override
    public PageDTO<Board> getPaginationList(int page) {

        long total = boardRepository.count();
        long start = (page-1) * super.pageSize+1;
        long totalPage = (long) Math.ceil((double)total/(double)pageSize);
        long startPage = ((page-1)/rangeSize)*rangeSize+1;
        long endPage = (startPage+rangeSize-1)>totalPage?totalPage:(startPage+rangeSize-1);
        long prevPage = (page-1)>0?(page-1):-1;
        long nextPage = (page+1)<=totalPage?(page+1):-1;

        return new PageDTO(storedBoardRepository.getBoardList(page, start, super.pageSize),
                page,
                startPage,
                endPage,
                prevPage,
                nextPage);
    }
}









