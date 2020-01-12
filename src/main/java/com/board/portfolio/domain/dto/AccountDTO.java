package com.board.portfolio.domain.dto;

import com.board.portfolio.validation.anotation.AuthKeyExist;
import com.board.portfolio.validation.anotation.EmailDuplicate;
import com.board.portfolio.validation.anotation.EmailExist;
import com.board.portfolio.validation.anotation.NicknameDuplicate;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AccountDTO {
    @Data
    public static class SignUp extends PasswordDTO{
        @NotBlank(message = "{nickname.not.blank}")
        @Size(min=5,max=10,message = "{nickname.size}")
        @NicknameDuplicate(message = "{nickname.duplicate}")
        private String nickname;

        @NotBlank(message = "{email.not.blank}")
        @Email(message = "{email.email}")
        @Size(min=5,max=40,message = "{email.size}")
        @EmailDuplicate(message = "{email.duplicate}")
        private String email;
    }

    @Data
    public static class SignIn{
        private String email;
        private String password;
    }
    @Data
    public static class Auth{
        @NotBlank(message = "required email")
        @EmailExist(message = "fail authenticate")
        private String email;
        @NotBlank(message = "required authKey")
        @AuthKeyExist(message = "fail authenticate")
        private String authKey;
    }
}
