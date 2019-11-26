package com.board.portfolio.service;

import com.board.portfolio.domain.dto.AccountDTO;
import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(AccountDTO.SignUp dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = modelMapper.map(dto, Account.class);
        accountRepository.save(account);
    }
}
