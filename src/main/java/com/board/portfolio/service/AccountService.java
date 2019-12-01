package com.board.portfolio.service;

import com.board.portfolio.domain.dto.AccountDTO;
import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.exception.NotFoundEmailException;
import com.board.portfolio.mail.EmailSender;
import com.board.portfolio.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EmailSender emailSender;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(AccountDTO.SignUp dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = modelMapper.map(dto, Account.class);
        String authKey = accountRepository.save(account).getAuthKey();
        emailSender.sendAuthMail(dto.getEmail(), authKey);
    }
    @Transactional
    public void authenticate(AccountDTO.Auth dto){
        accountRepository
                .findByEmailAndAuthKey(dto.getEmail(), dto.getAuthKey())
                .orElseThrow(()->new NotFoundEmailException("fail to find email for authenticate"))
                .setAuth(true);
    }

}
