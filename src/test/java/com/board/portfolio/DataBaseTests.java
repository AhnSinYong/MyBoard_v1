package com.board.portfolio;

import com.board.portfolio.domain.entity.*;
import com.board.portfolio.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    LikeBoardRepository likeBoardRepository;
    @Autowired
    LikeCommentRepository likeCommentRepository;
    @Autowired
    FileAttachmentRepository fileAttachmentRepository;
    @Autowired
    AlarmRepository alarmRepository;

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
//
//    @Test
//    @Transactional
//    void saveMemberTableTest() {
//        Account account = insertMember();
//    }
//    @Test
//    @Transactional
//    void saveBoardTableTest(){
//        Board board = insertBoard();
//    }

    @Test
    @Transactional
    void findAccountTest(){
        Account account = findMember("admin");
    }
    @Test
    @Transactional
    void findCommentTest(){
        Comment comment_ = commentRepository.findById((long)1).orElse(null);

    }

    @Test
    @Transactional
    void findLikeBoardTest(){
        LikeBoard likeBoard = likeBoardRepository.findById("test-id").orElse(null);
    }

    @Test
    void findLikeCommentTest(){
//        LikeComment comment = likeCommentRepository.save(buildLikeComment());
        LikeComment commentLike = likeCommentRepository.findById("test-id").orElse(null);
    }

    @Test
    void findFileAtachmentTest(){
        FileAttachment fileAttachment = fileAttachmentRepository.findById("test-id").orElse(null);
    }

    @Test
    void findAlarmTest(){
        Alarm alarm = alarmRepository.findById("test-id").orElse(null);
    }





    private Account findMember(String email){
        return accountRepository.findById(email).orElse(null);
    }



}
