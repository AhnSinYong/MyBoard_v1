package com.board.portfolio.security.cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class JwtCookieUtil {
    private static String jwtTokenName;

    @Autowired
    public JwtCookieUtil(@Value("${jwt.token.name}") String jwtTokenName){
        this.jwtTokenName = jwtTokenName;
    }

    public static String getJwtCookieValue(HttpServletRequest req){
        Cookie cookie = findJwtCookie(req.getCookies()).orElse(new Cookie(jwtTokenName,""));
        return cookie.getValue();
    }
    public static Cookie createSignOutCookie(){
        Cookie cookie = new Cookie(jwtTokenName,"");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    private static Optional<Cookie> findJwtCookie(Cookie[] cookies){
        Cookie jwtCookie=null;
        if(cookies==null){
            return Optional.ofNullable(jwtCookie);
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(jwtTokenName)){
                jwtCookie = cookie;
            }
        }
        return Optional.ofNullable(jwtCookie);

    }

}
