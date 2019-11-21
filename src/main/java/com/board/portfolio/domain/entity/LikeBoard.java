package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "TB_LIKE_BOARD")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LikeBoard implements EntityDefaultValues{

    @Id
    @Column(name="ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "BID")
    @JsonManagedReference
    private Board board;

    @ManyToOne
    @JoinColumn(name="EMAIL")
    @JsonManagedReference
    private Member member;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @PrePersist
    @Override
    public void setDefaultValues() {
        this.regDate = Optional.ofNullable(this.regDate).orElse(new Date());
    }
}