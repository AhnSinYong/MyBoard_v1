package com.board.portfolio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void passwordEncodeTest(){
        String password = "abcd";
        System.out.println(passwordEncoder.encode(password));
    }

}