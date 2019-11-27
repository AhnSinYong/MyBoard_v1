package com.board.portfolio.security.account;

import com.board.portfolio.domain.entity.AccountRole;
import lombok.Data;

import java.util.Date;

//TODO password에 대한 엄격한 접근 제한이 필요할듯
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
}
