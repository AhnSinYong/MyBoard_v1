package com.board.portfolio.domain.dto;

import com.board.portfolio.validation.anotation.BoardIdExist;
import com.board.portfolio.validation.anotation.FileExtension;
import com.board.portfolio.validation.anotation.FileSize;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class BoardDTO {
    @Data
    public static class Write{
        @NotBlank(message = "please, enter \"title\"")
        @Size(min=1,max=50,message = "title must be at least 1 characters and at most 50 characters.")
        private String title;
        @NotBlank(message = "please, enter \"content\"")
        @Size(min=1,max=350,message = "content must be at least 1 characters and at most 350 characters.")
        private String content;
        @FileSize(fileSize = 1024*1024, nullable = true, message = "The maximum file size is 1MB.")
        @FileExtension(fileExtension = {"txt","hwp","png","jpg"}, nullable = true, message = "not allow extension.")
        private List<MultipartFile> fileList;

        public boolean isNullFileList(){
            return fileList==null;
        }
    }
    @Data
    public static class Update{
        @NotBlank(message = "please, enter \"title\"")
        @Size(min=1,max=50,message = "title must be at least 1 characters and at most 50 characters.")
        private String title;
        @NotBlank(message = "please, enter \"content\"")
        @Size(min=1,max=350,message = "content must be at least 1 characters and at most 350 characters.")
        private String content;

        @Valid
        private List<FileDTO> existFileInfoList;

        @FileSize(fileSize = 1024*1024, nullable = true, message = "The maximum file size is 1MB.")
        @FileExtension(fileExtension = {"txt","hwp","png","jpg"}, nullable = true, message = "not allow extension.")
        private List<MultipartFile> inputFileList;

        public boolean isNullFileList(){
            return inputFileList==null;
        }
        public boolean isExistFileId(String fileId){
            if(existFileInfoList==null){
                return false;
            }

            for(FileDTO dto : existFileInfoList){
                if(dto.getFileId().equals(fileId)){
                    return true;
                }
            }
            return false;
        }
    }

    @Data
    public static class Like{
        @NotNull
        @BoardIdExist(message = "post isn't exist")
        private Long boardId;
    }
}
