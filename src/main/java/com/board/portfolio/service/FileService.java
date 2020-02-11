package com.board.portfolio.service;

import com.board.portfolio.domain.dto.BoardDTO;
import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.BoardDetail;
import com.board.portfolio.domain.entity.FileAttachment;
import com.board.portfolio.exception.custom.FailDownLoadFileException;
import com.board.portfolio.exception.custom.FailSaveFileException;
import com.board.portfolio.repository.FileAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileService {

    private final FileAttachmentRepository fileAttachmentRepository;

    @Value("${file.save-path.attachment}")
    private String ATTACHMENT_FILE_PATH;
    @Value("${file.save-path.img}")
    private String IMG_FILE_PATH;

    public void saveFileAttachment(BoardDetail board, List<MultipartFile> multipartFileList, Account account) throws IOException {
        for(MultipartFile file : multipartFileList){
            String originName = file.getOriginalFilename();
            String extension = Arrays.stream(originName.split("\\.")).reduce((x, y)->y).get().toLowerCase();
            String saveName = UUID.randomUUID().toString()+"."+extension;

            byte[] bytes = file.getBytes();
            Path path = Paths.get(ATTACHMENT_FILE_PATH + saveName);
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
    @Cacheable(value = "fileList", key = "#boardId")
    public List<FileAttachment> getFileAttachment(long boardId, BoardDetail boardDetail){
        List fileList = boardDetail.getFileAttachmentList();
        return fileList;
    }

    public void download(HttpServletResponse res, FileAttachment file){
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
        File down_file = new File(ATTACHMENT_FILE_PATH+file.getSaveName());
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

    public void deleteFilePhysic(List<FileAttachment> fileAttachmentList){
        for(FileAttachment fileAttachment : fileAttachmentList){
            File file = new File(ATTACHMENT_FILE_PATH+fileAttachment.getSaveName());
            if(file.exists())
                file.delete();
        }
    }

    @CacheEvict(value = "fileList",key = "#boardId")
    public void updateFileList(List<FileAttachment> fileAttachmentList, BoardDTO.Update dto, long boardId, Account account ){
        if(fileAttachmentList==null){
            return;
        }
        for(FileAttachment file : fileAttachmentList){
            if(!dto.isExistFileId(file.getFileId())){
                deleteFilePhysic(Arrays.asList(file));
                fileAttachmentRepository.delete(file);
            }
        }

        try {
            if(!dto.isNullFileList()){
                saveFileAttachment(new BoardDetail(boardId), dto.getInputFileList(), account);
            }
        }
        catch (IOException e){
            throw new FailSaveFileException();
        }
    }
}
