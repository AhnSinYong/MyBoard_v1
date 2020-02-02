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
import com.board.portfolio.store.repository.StoredBoardRepository;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDateTime;
import java.util.*;

import static com.board.portfolio.util.StaticUtils.modelMapper;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardDetailRepository boardDetailRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final FileAttachmentRepository fileAttachmentRepository;
    private final BoardPagination boardPagination;
    private final StoredBoardRepository storedBoardRepository;

    private final String FILE_COMMON_PATH = "./src/main/resources/attachment/";

    @Transactional
    public PageDTO<Board> getPaginBoardList(int page){
        return boardPagination.getPaginationList(page);
    }


    @Transactional
    public void writePost(BoardDTO.Write boardDTO, AccountSecurityDTO accountDTO) {

        BoardDetail board = modelMapper.map(boardDTO, BoardDetail.class);
        Account account = modelMapper.map(accountDTO, Account.class);
        board.setAccount(account);
        board = storedBoardRepository.save(board);
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
//    @Cacheable(value = "post", key = "#boardId")
    @Transactional
    public Map readPost(long boardId, AccountSecurityDTO accountDTO) {
        BoardDetail boardDetail = storedBoardRepository.findById(boardId).orElseThrow(NotFoundPostException::new);
        List<FileAttachment> fileAttachmentList = boardDetail.getFileAttachmentList();

        boardDetail.increaseView(storedBoardRepository);

        Map data = new HashMap<String,Object>();
        data.put("post",boardDetail);
        data.put("fileList", fileAttachmentList);

        boolean isLikedPost = isLikedPost(boardId, accountDTO.getEmail());
        data.put("isLikedPost",isLikedPost);

        return data;
    }
    private boolean isLikedPost(Long boardId, String email){
        if(email==null){
            return false;
        }
        return likeBoardRepository.findByBoardAndAccount(new Board(boardId),new Account(email)).isPresent();

    }

//    @CacheEvict(value = "post", key = "#dto.boardId")
    @Transactional
    public Map likePost(BoardDTO.Like dto, AccountSecurityDTO accountDTO) {
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(NotFoundPostException::new);
        Account account = modelMapper.map(accountDTO, Account.class);

        Optional<LikeBoard> opLikeBoard =  likeBoardRepository.findByBoardAndAccount(board,account);

        if(opLikeBoard.isPresent()){//이미 "좋아요"를 누름
            likeBoardRepository.delete(opLikeBoard.get());
            board.decreaseLike(storedBoardRepository);
        }
        else{
            likeBoardRepository.save(new LikeBoard(board,account));
            board.increaseLike(storedBoardRepository);
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

//    @CacheEvict(value = "post", key = "#boardId")
    @Transactional
    public void deletePost(Long boardId, AccountSecurityDTO accountDTO) {

        BoardDetail board = boardDetailRepository.findById(boardId).orElseThrow(NotFoundPostException::new);
        if(!board.getAccount().getEmail().equals(accountDTO.getEmail())){
            throw new NotAllowAccessException();
        }

        List<FileAttachment> fileAttachmentList = board.getFileAttachmentList();
        deleteFilePhysic(fileAttachmentList);
        storedBoardRepository.delete(board);
    }
    private void deleteFilePhysic(List<FileAttachment> fileAttachmentList){
        for(FileAttachment fileAttachment : fileAttachmentList){
            File file = new File(FILE_COMMON_PATH+fileAttachment.getSaveName());
            if(file.exists())
                file.delete();
        }
    }

//    @CacheEvict(value = "post", key = "#boardId")
    @Transactional
    public void updatePost(Long boardId, BoardDTO.Update dto, AccountSecurityDTO accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);
        BoardDetail board = storedBoardRepository.findById(boardId).orElseThrow(NotFoundPostException::new);
        if(!board.getAccount().getEmail().equals(accountDTO.getEmail())){
            throw new NotAllowAccessException();
        }

        board.updatePost(dto.getTitle(),
                dto.getContent(),
                LocalDateTime.now(),
                storedBoardRepository);

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
