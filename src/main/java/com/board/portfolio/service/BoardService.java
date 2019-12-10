package com.board.portfolio.service;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.domain.entity.*;
import com.board.portfolio.exception.FailSaveFileException;
import com.board.portfolio.exception.NotFoundPostException;
import com.board.portfolio.paging.BoardPagination;
import com.board.portfolio.paging.PageDTO;
import com.board.portfolio.repository.BoardDetailRepository;
import com.board.portfolio.repository.FileAttachmentRepository;
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

    private BoardDetailRepository boardDetailRepository;
    private FileAttachmentRepository fileAttachmentRepository;
    private BoardPagination boardPagination;
    private ModelMapper modelMapper;

    @Autowired
    public BoardService(BoardDetailRepository boardDetailRepository,
                        FileAttachmentRepository fileAttachmentRepository,
                        BoardPagination boardPagination,
                        ModelMapper modelMapper){
        this.boardDetailRepository = boardDetailRepository;
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
    public Map readPost(long boardId) {
        BoardDetail boardDetail = boardDetailRepository.findById(boardId).orElseThrow(()->new NotFoundPostException());
        List<FileAttachment> fileAttachmentList = boardDetail.getFileAttachmentList();
        List<Comment> commentList = boardDetail.getCommentList();

        Map data = new HashMap<String,Object>();
        data.put("post",boardDetail);
        data.put("fileList", fileAttachmentList);
        data.put("commentList",commentList);
        return data;
    }
}
