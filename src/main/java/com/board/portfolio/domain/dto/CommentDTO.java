package com.board.portfolio.domain.dto;

import com.board.portfolio.validation.anotation.BoardIdExist;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentDTO {
    @Data
    public static class Write{
        @NotBlank(message = "not allow blank")
        @Size(min=1, max=150)
        private String content;
    }
    @Data
    public static class Reply{
        @NotNull
        @BoardIdExist
        private Long boardId;
        @NotNull
        private Long commentId;
        @NotBlank(message = "not allow blank")
        @Size(min=1, max=150)
        private String content;
    }
    @Data
    public static class Modify{
        @NotBlank(message = "not allow blank")
        @Size(min=1, max=150)
        private String content;
    }
}
