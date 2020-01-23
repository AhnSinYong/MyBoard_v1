package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "TB_FILE_ATTACHMENT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileAttachment extends EntityDefaultValues{

    @Id
    @Column(name="FILE_ID")
    private String fileId;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    @JsonManagedReference
    private BoardDetail board;

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
    private Account account;

    public void increaseDown(){
        this.down++;
    }

    @Override
    public void setDefaultValues() {
        this.fileId = UUID.randomUUID().toString();
        this.saveDate = Optional.ofNullable(this.saveDate).orElse(new Date());
        this.down = Optional.ofNullable(this.down).orElse(0);
    }
}
