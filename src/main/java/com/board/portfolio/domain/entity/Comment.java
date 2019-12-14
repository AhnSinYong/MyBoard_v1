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
@Table(name = "TB_COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment implements EntityDefaultValues{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENT_ID")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    @JsonManagedReference
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
    private Account account;

    @Column(name="GROUP_")
    private Long group;

    @Column(name="DEL_PARENT_CNT")
    private Integer delParentCnt;

    @Column(name="HAS_DEL_TYPE_PARENT")
    private boolean hasDelTypeParent;

    @Column(name="TYPE")
    @Enumerated(value = EnumType.STRING)
    private CommentType type;

    @OneToMany(mappedBy = "comment")
    @JsonBackReference
    private List<LikeComment> likeCommentList;

    public void increaseLike(){
        this.like++;
    }

    public void decreaseLike(){
        this.like--;
    }
    public void increaseDelParentCnt(){
        this.delParentCnt++;
    }
    public void increaseDelParentCnt(int cnt){
        this.delParentCnt += cnt;
    }
    @PrePersist
    @Override
    public void setDefaultValues() {
        this.like = Optional.ofNullable(this.like).orElse(0);
        this.regDate = Optional.ofNullable(this.regDate).orElse(new Date());
        this.delParentCnt = Optional.ofNullable(this.delParentCnt).orElse(0);
    }
}
