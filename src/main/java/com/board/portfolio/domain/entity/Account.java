package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(
        name = "TB_ACCOUNT",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"NICKNAME","SOCIAL_ID"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account implements EntityDefaultValues {

    @Id
    @Column(name="EMAIL")
    private String email;

    @Column(name="PASSWORD")
    @JsonIgnore
    private String password;

    @Column(name="NICKNAME")
    private String nickname;

    @Column(name="SIGNUP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date signUpDate = new Date();

    @Column(name="ROLE")
    @Enumerated(value= EnumType.STRING)
    private Role role;

    @Column(name="SOCIAL_ID")
    private String socialId;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<LikeBoard> likeBoardList = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<LikeComment> likeCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<FileAttachment> fileAttachmentList = new ArrayList<>();

    @PrePersist
    @Override
    public void setDefaultValues() {
        this.signUpDate = Optional.ofNullable(this.signUpDate).orElse(new Date());
        this.role = Optional.ofNullable(this.role).orElse(Role.MEMBER);
    }

}