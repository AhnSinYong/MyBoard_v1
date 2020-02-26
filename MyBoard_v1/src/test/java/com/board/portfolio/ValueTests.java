package com.board.portfolio;

import com.board.portfolio.domain.entity.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
class ValueTests {

    @Test
    void enumValueTest(){
        System.out.println(AccountRole.ADMIN.getRoleName());
        System.out.println(AccountRole.MEMBER.getRoleName());

        System.out.println(AccountRole.valueOf("ADMIN").getRoleName());
    }
}
