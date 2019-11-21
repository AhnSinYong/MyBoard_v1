package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "TB_BOARD")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Board implements EntityDefaultValues{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BID")
    private Long bId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

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
    private Member member;

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<LikeBoard> likeBoardList = new ArrayList<>();


    @PrePersist
    @Override
    public void setDefaultValues() {
        this.like = Optional.ofNullable(this.like).orElse(0);
        this.view = Optional.ofNullable(this.view).orElse(0);
        this.regDate = Optional.ofNullable(this.regDate).orElse(new Date());
    }
}
