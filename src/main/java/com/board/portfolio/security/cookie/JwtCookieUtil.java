package com.board.portfolio.security.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public class JwtCookieUtil {
    private static final String jwtTokenName = "jwt-token";
    public static String getJwtCookieValue(HttpServletRequest req){
        Cookie cookie = findJwtCookie(req.getCookies()).orElse(new Cookie(jwtTokenName,""));
        return cookie.getValue();
    }
    private static Optional<Cookie> findJwtCookie(Cookie[] cookies){
        Cookie jwtCookie=null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(jwtTokenName)){
                jwtCookie = cookie;
            }
        }
        return Optional.ofNullable(jwtCookie);

    }
}
