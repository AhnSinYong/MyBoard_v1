package com.board.portfolio.service;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.Board;
import com.board.portfolio.domain.entity.FileAttachment;
import com.board.portfolio.exception.FailSaveFileException;
import com.board.portfolio.paging.BoardPagination;
import com.board.portfolio.paging.PageDTO;
import com.board.portfolio.repository.BoardRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private FileAttachmentRepository fileAttachmentRepository;
    private BoardPagination boardPagination;
    private ModelMapper modelMapper;

    @Autowired
    public BoardService(BoardRepository boardRepository,
                        FileAttachmentRepository fileAttachmentRepository,
                        BoardPagination boardPagination,
                        ModelMapper modelMapper){
        this.boardRepository = boardRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.boardPagination = boardPagination;
        this.modelMapper = modelMapper;
    }

    public PageDTO<Board> getPaginBoardList(int page){
        return boardPagination.getPaginationList(page);
    }

    @Transactional
    public void writePost(BoardDTO.Write boardDTO, AccountSecurityDTO accountDTO) {

        Board board = modelMapper.map(boardDTO, Board.class);
        Account account = modelMapper.map(accountDTO, Account.class);
        board.setAccount(account);
        board = boardRepository.save(board);
        try {
            if(!boardDTO.isNullFileList()){
                saveFileAttachment(board, boardDTO.getFileList(), account);
            }
        }
        catch (IOException e){
            throw new FailSaveFileException("fail to save file");
        }

    }
    private void saveFileAttachment(Board board, List<MultipartFile> multipartFileList, Account account) throws IOException{
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

}
