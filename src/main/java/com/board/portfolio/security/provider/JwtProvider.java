package com.board.portfolio.security.provider;

import com.board.portfolio.security.token.JwtPreToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtPreToken token = (JwtPreToken) authentication;
        if(isAnonymous(token)){
            return token;
        }




        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtPreToken.class.isAssignableFrom(aClass);
    }

    private boolean isAnonymous(JwtPreToken token){
        return token.getToken().equals("");
    }
}
