package com.board.portfolio.domain.entity;

import javax.persistence.PrePersist;
import java.util.Optional;


//TODO 좀 더 효율적으로 default 값을 설정 할 수 있도록 고민필요(동적 객체 생성, Class.class 등)
public interface EntityDefaultValues {
    void setDefaultValues();
}
