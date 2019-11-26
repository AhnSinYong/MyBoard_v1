package com.board.portfolio.controller;

import com.board.portfolio.domain.dto.AccountDTO;
import com.board.portfolio.service.AccountService;
import com.board.portfolio.validation.validator.AccountSignUpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final String SUCCESS = "SUCCESS";
    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    public ResponseEntity signUp(@RequestBody @Valid AccountDTO.SignUp dto){
        accountService.signUp(dto);
        return ResponseEntity.ok(Result.SUCCESS);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(new AccountSignUpValidator());
    }
}
