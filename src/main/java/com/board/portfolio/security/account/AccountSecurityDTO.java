package com.board.portfolio.security.account;

import com.board.portfolio.domain.entity.AccountRole;
import lombok.Data;

import java.util.Date;

@Data
public class AccountSecurityDTO {
    private String email;
    private String password;
    private String nickname;
    private Date signUpDate;
    private AccountRole role;
    private String socialId;
    private String authKey;
    private boolean isAuth;

    public AccountSecurityDTO(){

    }

    public AccountSecurityDTO(String email,String nickname, AccountRole role) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
