package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class BoardCore extends EntityDefaultValues{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOARD_ID")
    private Long boardId;

    @Column(name = "TITLE")
    private String title;

    @Column(name="LIKE_")
    private Integer like;

    @Column(name="VIEW")
    private Integer view;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Column(name = "UP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upDate;

    @ManyToOne
    @JoinColumn(name="EMAIL")
    @JsonManagedReference
    private Account account;


    public BoardCore(Long boardId) {
        this.boardId = boardId;
    }

    public void increaseLike(){
        this.like++;
    }
    public void decreaseLike(){
        this.like--;
    }
    public void increaseView(){
        this.view++;
    }

    @Override
    public void setDefaultValues() {
        this.like = Optional.ofNullable(this.like).orElse(0);
        this.view = Optional.ofNullable(this.view).orElse(0);
        this.regDate = Optional.ofNullable(this.regDate).orElse(new Date());
    }
}
