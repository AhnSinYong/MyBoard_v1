package com.board.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
class DataBaseTests {
    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER;
    @Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.username}")
    private String USER;
    @Value("${spring.datasource.password}")
    private String PW;
    @Test
    void dbConnectionTest() throws ClassNotFoundException {
        System.out.println(USER);
        System.out.println(PW);
        Class.forName(DRIVER);
        try(Connection con = DriverManager.getConnection(URL, USER, PW)) {
            System.out.println(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
