package com.board.portfolio.security.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountDetailsTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void encoderTest(){
        String pw = "1q2w3e!@#";
        String enPw = passwordEncoder.encode(pw);

        String pw1 = "1q2w3e!@#";
        String enPw1 = passwordEncoder.encode(pw);

        System.out.println(enPw.equals(enPw1));
    }

}