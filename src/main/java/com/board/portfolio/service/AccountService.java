package com.board.portfolio.service;

import com.board.portfolio.domain.dto.AccountDTO;
import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.exception.custom.NotFoundEmailException;
import com.board.portfolio.mail.EmailSender;
import com.board.portfolio.mail.manager.AuthMail;
import com.board.portfolio.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(AccountDTO.SignUp dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = modelMapper.map(dto, Account.class);
        account = accountRepository.save(account);
        String authKey = account.getAuthKey();
        LocalDateTime signUpDate = account.getSignUpDate();
        emailSender.sendAuthMail(new AuthMail(dto.getEmail(), signUpDate, authKey));

    }
    @Transactional
    public void authenticate(AccountDTO.Auth dto){
        String email = dto.getEmail();
        String authKey = dto.getAuthKey();
        accountRepository
                .findByEmailAndAuthKey(email, authKey)
                .orElseThrow(NotFoundEmailException::new)
                .setAuth(true);
        emailSender.completeAuthMail(email);
    }

}
