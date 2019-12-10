package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "TB_BOARD")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardDetail extends BoardCore{

    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<Comment> commentList;

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<LikeBoard> likeBoardList;

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<FileAttachment> fileAttachmentList;
}
