package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "TB_COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment implements EntityDefaultValues{

    @Id
    @Column(name="CID")
    private String cId;

    @ManyToOne
    @JoinColumn(name = "BID")
    private Board board;

    @Column(name = "CONTENT")
    private String content;

    @Column(name="LIKE_")
    private Integer like;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Column(name = "UP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upDate;

    @ManyToOne
    @JoinColumn(name="EMAIL")
    @JsonManagedReference
    private Member member;


    @PrePersist
    @Override
    public void setDefaultValues() {
        this.like = Optional.ofNullable(this.like).orElse(0);
        this.regDate = Optional.ofNullable(this.regDate).orElse(new Date());
        System.out.println(this.cId);
    }
}
