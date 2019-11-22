package com.board.portfolio;

import com.board.portfolio.domain.entity.*;
import com.board.portfolio.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.UUID;

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
    MemberRepository memberRepository;
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

    @Test
    @Transactional
    void saveMemberTableTest() {
        Member member = insertMember();
    }
    @Test
    @Transactional
    void saveBoardTableTest(){
        Board board = insertBoard();
    }

    @Test
    @Transactional
    void findMemberTest(){
        Member member = findMember("test@naver.com");
        List<Board> boardList = member.getBoardList();
        Assertions.assertEquals("title",boardList.get(0).getTitle());

    }
    @Test
    @Transactional
    void findCommentTest(){
        Comment comment_ = commentRepository.findById("test-for-custom-id").orElse(null);

    }

    @Test
    @Transactional
    void findLikeBoardTest(){
        LikeBoard likeBoard = likeBoardRepository.findById("test-for-custom-id").orElse(null);
    }

    @Test
    void findLikeCommentTest(){
//        LikeComment comment = likeCommentRepository.save(buildLikeComment());
        LikeComment commentLike = likeCommentRepository.findById("test-for-custom-id").orElse(null);
    }

    @Test
    void findFileAtachmentTest(){
        FileAttachment fileAttachment = fileAttachmentRepository.findById("test-for-custom-id").orElse(null);
    }





    private Member findMember(String email){
        return memberRepository.findById(email).orElse(null);
    }

    private Member insertMember(){
        Member member = buildMember();
        return memberRepository.save(member);
    }
    private Member buildMember(){
        return Member.builder()
                .email("test@naver.com")
                .password("1234")
                .nickname("admin")
                .socialId(UUID.randomUUID().toString())
                .build();
    }
    private Board insertBoard(){
        Board board = buildBoard();
        return boardRepository.save(board);
    }
    private Board buildBoard(){
        return Board.builder()
                .title("title")
                .content("content")
                .member(buildMember()).build();
    }
    private Board buildBoard(Long bId){
        return Board.builder()
                .bId(bId)
                .title("title")
                .content("content")
                .member(buildMember()).build();
    }
    private Comment insertComment(Long bId){
        Comment comment = buildComment(bId);
        return commentRepository.save(comment);
    }
    private Comment buildComment(){
        return Comment.builder()
                .cId("test-for-custom-id")
                .board(Board.builder().bId((long)29).build())
                .content("testtest")
                .member(Member.builder().email("test@naver.com").build())
                .build();
    }
    private Comment buildComment(Long bId){
        return Comment.builder()
                .cId("test-for-custom-id")
                .board(Board.builder().bId((long)29).build())
                .content("testtest")
                .member(Member.builder().email("test@naver.com").build())
                .build();
    }

    private LikeBoard insertLikeBoard(Long bId){
        LikeBoard likeBoard = buildLikeBoard(bId);
        return likeBoardRepository.save(likeBoard);
    }

    private LikeBoard buildLikeBoard(Long bId){
        return LikeBoard.builder()
                .id("test-for-custom-id")
                .board(buildBoard(bId))
                .member(buildMember())
                .build();
    }

    private LikeComment buildLikeComment(){
        return LikeComment.builder()
                .id("test-for-custom-id")
                .comment(buildComment())
                .member(buildMember())
                .build();
    }


}
