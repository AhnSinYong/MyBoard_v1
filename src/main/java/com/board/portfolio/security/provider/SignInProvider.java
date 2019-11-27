package com.board.portfolio.security.provider;


import com.board.portfolio.security.account.AccountDetails;
import com.board.portfolio.security.account.AccountDetailsService;
import com.board.portfolio.security.token.SignInPreToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SignInProvider implements AuthenticationProvider {
    @Autowired
    AccountDetailsService detailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SignInPreToken token = (SignInPreToken) authentication;
        AccountDetails detail = (AccountDetails) detailsService.loadUserByUsername(token.getEmail());
        detail.checkSignInProcess(token, passwordEncoder);

        return detail.getPostToken(detail);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SignInPreToken.class.isAssignableFrom(aClass) ;
    }
}
