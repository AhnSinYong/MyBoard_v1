package com.board.portfolio.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignOutFilter extends AbstractAuthenticationProcessingFilter {

    public SignOutFilter(String defaultUrl){
       super(defaultUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        Cookie cookie = new Cookie("jwt-token","");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return null;
    }
}
