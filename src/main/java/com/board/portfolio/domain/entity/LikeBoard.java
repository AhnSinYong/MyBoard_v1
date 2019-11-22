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
    @Column(name="LIKE_BOARD_ID")
    private String likeBoardId;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    @JsonManagedReference
    private Board board;

    @ManyToOne
    @JoinColumn(name="EMAIL")
    @JsonManagedReference
    private Account account;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @PrePersist
    @Override
    public void setDefaultValues() {
        this.regDate = Optional.ofNullable(this.regDate).orElse(new Date());
    }
}
