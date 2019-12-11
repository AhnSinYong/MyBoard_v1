package com.board.portfolio.service;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.domain.entity.*;
import com.board.portfolio.exception.FailSaveFileException;
import com.board.portfolio.exception.NotFoundPostException;
import com.board.portfolio.paging.BoardPagination;
import com.board.portfolio.paging.PageDTO;
import com.board.portfolio.repository.BoardDetailRepository;
import com.board.portfolio.repository.BoardRepository;
import com.board.portfolio.repository.FileAttachmentRepository;
import com.board.portfolio.repository.LikeBoardRepository;
import com.board.portfolio.security.account.AccountSecurityDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private BoardDetailRepository boardDetailRepository;
    private LikeBoardRepository likeBoardRepository;
    private FileAttachmentRepository fileAttachmentRepository;
    private BoardPagination boardPagination;
    private ModelMapper modelMapper;

    @Autowired
    public BoardService(BoardRepository boardRepository,
                        BoardDetailRepository boardDetailRepository,
                        LikeBoardRepository likeBoardRepository,
                        FileAttachmentRepository fileAttachmentRepository,
                        BoardPagination boardPagination,
                        ModelMapper modelMapper){
        this.boardRepository = boardRepository;
        this.boardDetailRepository = boardDetailRepository;
        this.likeBoardRepository = likeBoardRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.boardPagination = boardPagination;
        this.modelMapper = modelMapper;
    }

    public PageDTO<Board> getPaginBoardList(int page){
        return boardPagination.getPaginationList(page);
    }

    @Transactional
    public void writePost(BoardDTO.Write boardDTO, AccountSecurityDTO accountDTO) {

        BoardDetail board = modelMapper.map(boardDTO, BoardDetail.class);
        Account account = modelMapper.map(accountDTO, Account.class);
        board.setAccount(account);
        board = boardDetailRepository.save(board);
        try {
            if(!boardDTO.isNullFileList()){
                saveFileAttachment(board, boardDTO.getFileList(), account);
            }
        }
        catch (IOException e){
            throw new FailSaveFileException("fail to save file");
        }

    }
    private void saveFileAttachment(BoardDetail board, List<MultipartFile> multipartFileList, Account account) throws IOException{
        for(MultipartFile file : multipartFileList){
            String originName = file.getOriginalFilename();
            String saveName = UUID.randomUUID().toString();
            String extension = Arrays.stream(originName.split("\\.")).reduce((x,y)->y).get().toLowerCase();

            byte[] bytes = file.getBytes();
            Path path = Paths.get("./src/main/resources/attachment/" + saveName +"."+extension);
            Files.write(path, bytes);

            FileAttachment fileAttachment = FileAttachment.builder()
                    .board(board)
                    .originName(originName)
                    .saveName(saveName)
                    .extension(extension)
                    .account(account)
                    .build();

            fileAttachmentRepository.save(fileAttachment);
        }
    }

    @Transactional
    public Map readPost(long boardId, AccountSecurityDTO accountDTO) {
        BoardDetail boardDetail = boardDetailRepository.findById(boardId).orElseThrow(()->new NotFoundPostException());
        List<FileAttachment> fileAttachmentList = boardDetail.getFileAttachmentList();
        List<Comment> commentList = boardDetail.getCommentList();

        boardDetail.increaseView();

        Map data = new HashMap<String,Object>();
        data.put("post",boardDetail);
        data.put("fileList", fileAttachmentList);
        data.put("commentList",commentList);

        String email = accountDTO.getEmail();


        boolean isLikedPost = isLikedPost(boardDetail.getLikeBoardList(), email);
        data.put("isLikedPost",isLikedPost);


        return data;
    }
    private boolean isLikedPost(List<LikeBoard> likeBoardList, String email){
        if(email==null){
            return false;
        }

        boolean isLikedBoard = false;

        for(LikeBoard likeBoard : likeBoardList){
            String likeEmail = likeBoard.getAccount().getEmail();
            if(likeEmail.equals(email)){
                isLikedBoard = true;
                break;
            }
        }
        return isLikedBoard;
    }

    @Transactional
    public Map likePost(BoardDTO.Like dto, AccountSecurityDTO accountDTO) {
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(()->new NotFoundPostException());
        Account account = modelMapper.map(accountDTO, Account.class);

        Optional<LikeBoard> opLikeBoard =  likeBoardRepository.findByBoardAndAccount(board,account);

        if(opLikeBoard.isPresent()){//이미 "좋아요"를 누름
            likeBoardRepository.delete(opLikeBoard.get());
            board.decreaseLike();
        }
        else{
            likeBoardRepository.save(new LikeBoard(board,account));
            board.increaseLike();
        }

        Map data = new HashMap<String,Object>();
        data.put("like",board.getLike());
        return data;

    }
}
