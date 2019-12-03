package com.board.portfolio.paging;

public abstract class Pagination {
    public int pageSize;
    public int rangeSize;

    public Pagination(int pageSize, int rangeSize){
        this.pageSize = pageSize;
        this.rangeSize = rangeSize;
    }

    public abstract PageDTO getPaginationList(int page);

}
