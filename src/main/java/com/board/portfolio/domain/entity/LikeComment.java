package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "TB_LIKE_COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LikeComment implements EntityDefaultValues{

    @Id
    @Column(name="LIKE_COMMENT_ID")
    private String likeCommentId;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    @JsonManagedReference
    private Comment comment;

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
