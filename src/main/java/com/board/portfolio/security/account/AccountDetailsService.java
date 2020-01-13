package com.board.portfolio.security.account;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.exception.custom.NotFoundEmailException;
import com.board.portfolio.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findById(email).orElseThrow(NotFoundEmailException::new);
        return AccountDetails.transFromAccountWithSave(account);
    }
}
