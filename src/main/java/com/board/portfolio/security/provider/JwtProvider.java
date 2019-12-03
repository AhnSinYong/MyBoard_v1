package com.board.portfolio.security.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.board.portfolio.security.account.AccountDetails;
import com.board.portfolio.security.account.AccountDetailsService;
import com.board.portfolio.security.jwt.JwtDecoder;
import com.board.portfolio.security.token.JwtPreToken;
import com.board.portfolio.security.token.SignInPostToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider implements AuthenticationProvider {

    @Autowired
    JwtDecoder jwtDecoder;
    @Autowired
    AccountDetailsService detailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JwtPreToken token = (JwtPreToken) authentication;
        if(isAnonymous(token)){
            return token;
        }
        DecodedJWT decodedJWT = jwtDecoder.decodeJwt(token.getToken());

        SignInPostToken signInPostToken = (SignInPostToken) SecurityContextHolder.getContext().getAuthentication();

        if(signInPostToken == null){
            String email = decodedJWT.getClaim("email").asString();
            AccountDetails details = (AccountDetails) detailsService.loadUserByUsername(email);
            return details.getPostToken(details);
        }
        return signInPostToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtPreToken.class.isAssignableFrom(aClass);
    }

    private boolean isAnonymous(JwtPreToken token){
        return token.getToken().equals("");
    }
}
