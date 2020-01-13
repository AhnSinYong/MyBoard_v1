package com.board.portfolio.service;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.domain.entity.*;
import com.board.portfolio.exception.custom.*;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
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

    private final String FILE_COMMON_PATH = "./src/main/resources/attachment/";

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
            throw new FailSaveFileException();
        }

    }
    private void saveFileAttachment(BoardDetail board, List<MultipartFile> multipartFileList, Account account) throws IOException{
        for(MultipartFile file : multipartFileList){
            String originName = file.getOriginalFilename();
            String extension = Arrays.stream(originName.split("\\.")).reduce((x,y)->y).get().toLowerCase();
            String saveName = UUID.randomUUID().toString()+"."+extension;

            byte[] bytes = file.getBytes();
            Path path = Paths.get(FILE_COMMON_PATH + saveName);
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
        BoardDetail boardDetail = boardDetailRepository.findById(boardId).orElseThrow(NotFoundPostException::new);
        List<FileAttachment> fileAttachmentList = boardDetail.getFileAttachmentList();

        boardDetail.increaseView();

        Map data = new HashMap<String,Object>();
        data.put("post",boardDetail);
        data.put("fileList", fileAttachmentList);

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
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(NotFoundPostException::new);
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

    @Transactional
    public void download(HttpServletResponse res, String fileId) {
        FileAttachment file = fileAttachmentRepository.findById(fileId).orElseThrow(NotFoundFileException::new);

        setDownloadHeader(res,file);
        executeDownload(res,file);
    }
    private void setDownloadHeader(HttpServletResponse res, FileAttachment file){
        try{
            String docName = URLEncoder.encode(file.getOriginName(),"UTF-8").replaceAll("\\+", "%20"); //한글파일명 깨지지 않도록
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition",
                    "attachment;filename="+docName+";");
        }
        catch (IOException e){
            throw new FailDownLoadFileException();
        }
    }

    private void executeDownload(HttpServletResponse res, FileAttachment file){
        File down_file = new File(FILE_COMMON_PATH+file.getSaveName());
        try(FileInputStream fileIn = new FileInputStream(down_file);
            ServletOutputStream out = res.getOutputStream();){
            byte[] outputByte = new byte[4096];
            while(fileIn.read(outputByte, 0, 4096) != -1)
            {
                out.write(outputByte, 0, 4096);
            }
            out.flush();
            file.increaseDown();
        }
        catch (IOException e){
            throw new FailDownLoadFileException();
        }

    }

    @Transactional
    public void deletePost(Long boardId, AccountSecurityDTO accountDTO) {

        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundPostException::new);
        if(!board.getAccount().getEmail().equals(accountDTO.getEmail())){
            throw new NotAllowAccessException("not allow access");
        }

        List<FileAttachment> fileAttachmentList = board.getFileAttachmentList();
        deleteFilePhysic(fileAttachmentList);
        boardRepository.delete(board);
    }
    private void deleteFilePhysic(List<FileAttachment> fileAttachmentList){
        for(FileAttachment fileAttachment : fileAttachmentList){
            File file = new File(FILE_COMMON_PATH+fileAttachment.getSaveName());
            if(file.exists())
                file.delete();
        }
    }

    @Transactional
    public void updatePost(Long boardId, BoardDTO.Update dto, AccountSecurityDTO accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);
        BoardDetail board = boardDetailRepository.findById(boardId).orElseThrow(NotFoundPostException::new);
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setUpDate(new Date());

        List<FileAttachment> fileAttachmentList = board.getFileAttachmentList();


        for(FileAttachment file : fileAttachmentList){
            if(!dto.isExistFileId(file.getFileId())){
                deleteFilePhysic(Arrays.asList(file));
                fileAttachmentRepository.delete(file);
            }
        }

        try {
            if(!dto.isNullFileList()){
                saveFileAttachment(board, dto.getInputFileList(), account);
            }
        }
        catch (IOException e){
            throw new FailSaveFileException();
        }


    }
}
