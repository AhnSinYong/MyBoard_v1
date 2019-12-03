package com.board.portfolio.controller;

import com.board.portfolio.domain.dto.AccountDTO;
import com.board.portfolio.service.AccountService;
import com.board.portfolio.validation.validator.AccountAuthValidator;
import com.board.portfolio.validation.validator.AccountSignUpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController {
    private AccountService accountService;
    private AccountSignUpValidator accountSignUpValidator;
    private AccountAuthValidator accountAuthValidator;

    @InitBinder("signUp")
    protected void initBinderSignUp(WebDataBinder binder){ binder.addValidators(accountSignUpValidator); }
    @InitBinder("auth")
    protected void initBinderAuth(WebDataBinder binder){ binder.addValidators(accountAuthValidator); }

    @Autowired
    public AccountController(AccountService accountService,
                             AccountSignUpValidator accountSignUpValidator,
                             AccountAuthValidator accountAuthValidator){
        this.accountService = accountService;
        this.accountSignUpValidator = accountSignUpValidator;
        this.accountAuthValidator = accountAuthValidator;
    }

    @PostMapping("/account")
    public ResponseEntity signUp(@RequestBody @Valid AccountDTO.SignUp dto){
        accountService.signUp(dto);
        return ResponseEntity.ok(Result.SUCCESS);
    }
    @GetMapping("/authenticate")
    public ResponseEntity authenticate(@Valid AccountDTO.Auth dto){
        accountService.authenticate(dto);
        return ResponseEntity.ok(Result.SUCCESS);
    }
}
