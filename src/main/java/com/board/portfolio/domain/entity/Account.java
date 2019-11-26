package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
    private AccountRole role;

    @Column(name="SOCIAL_ID")
    private String socialId;

    @Column(name="AUTH_KEY")
    private String authKey;

    @Column(name="IS_AUTH")
    private boolean isAuth;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Board> boardList;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Comment> commentList;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<LikeBoard> likeBoardList;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<LikeComment> likeCommentList;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<FileAttachment> fileAttachmentList;

    @OneToMany(mappedBy = "targetAccount")
    @JsonBackReference
    private List<Alarm> myAlarmList;

    @OneToMany(mappedBy = "triggerAccount")
    @JsonBackReference
    private List<Alarm> triggerAlarmList;

    @PrePersist
    @Override
    public void setDefaultValues() {
        this.signUpDate = Optional.ofNullable(this.signUpDate).orElse(new Date());
        this.role = Optional.ofNullable(this.role).orElse(AccountRole.MEMBER);
    }

}
