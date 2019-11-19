package com.board.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        //livereload 설정
        //참고 : https://haviyj.tistory.com/11
        System.setProperty("spring.devtools.restart.enabled","false");
        System.setProperty("spring.devtools.livereload.enabled","true");
        SpringApplication.run(PortfolioApplication.class, args);
    }

}
