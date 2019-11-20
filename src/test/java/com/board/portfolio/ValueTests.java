package com.board.portfolio;

import com.board.portfolio.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
class ValueTests {

    @Test
    void enumValueTest(){
        System.out.println(Role.ADMIN.getRoleName());
        System.out.println(Role.MEMBER.getRoleName());

        System.out.println(Role.valueOf("ADMIN").getRoleName());
    }
}
