package com.board.portfolio.mail.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMail {
    private String email;
    private Date sendDate;
    private String authKey;
}
