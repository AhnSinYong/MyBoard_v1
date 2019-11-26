package com.board.portfolio.domain.dto;

import com.board.portfolio.validation.anotation.EmailUnique;
import com.board.portfolio.validation.anotation.NicknameUnique;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AccountDTO {
    @Data
    public static class SignUp{
        @NotBlank(message = "please, enter \"nickname\"")
        @Size(min=5,max=10,message = "Nickname must be at least 5 characters and at most 10 characters.")
        @NicknameUnique(message = "This email is already exist.")
        private String nickname;

        @NotBlank(message = "please, enter \"email\"")
        @Email(message = "not email format")
        @Size(min=5,max=40,message = "Email must be at least 5 characters and at most 40 characters.")
        @EmailUnique(message = "This email is already registered.")
        private String email;

        @NotBlank(message = "please, enter \"password\"")
        @Pattern(regexp ="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$"
                ,message = "Passwords must be at least 8 characters in length, including English, numbers and special characters.")//영문,숫자,특문 8글자 이상
        private String password;

        @NotBlank(message = "please, enter \"password check \"")
        private String passwordCheck;
    }
}
