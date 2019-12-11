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
public class Board extends BoardCore {
    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<Comment> commentList;

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<LikeBoard> likeBoardList;

    @OneToMany(mappedBy = "board")
    @JsonBackReference
    private List<FileAttachment> fileAttachmentList;

    public Board(Long boardId) {
        super(boardId);
    }
}
