package com.board.portfolio.mail.manager;

import com.board.portfolio.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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