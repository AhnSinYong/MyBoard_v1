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
@Table(name = "TB_FILE_ATTACHMENT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileAttachment implements EntityDefaultValues{

    @Id
    @Column(name="FID")
    private String cId;

    @ManyToOne
    @JoinColumn(name = "BID")
    @JsonManagedReference
    private Board board;

    @Column(name = "ORIGIN_NAME")
    private String originName;

    @Column(name="SAVE_NAME")
    private String saveName;

    @Column(name="EXTENSION")
    private String extension;

    @Column(name="DOWN")
    private Integer down;

    @Column(name = "SAVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saveDate;

    @ManyToOne
    @JoinColumn(name="EMAIL")
    @JsonManagedReference
    private Member member;


    @PrePersist
    @Override
    public void setDefaultValues() {
        this.saveDate = Optional.ofNullable(this.saveDate).orElse(new Date());
        this.down = Optional.ofNullable(this.down).orElse(0);
    }
}
