package com.board.portfolio.mail.manager;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthMailManagerTest {

    @Autowired
    AuthMailManager manager;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void managerTest(){


    }

}