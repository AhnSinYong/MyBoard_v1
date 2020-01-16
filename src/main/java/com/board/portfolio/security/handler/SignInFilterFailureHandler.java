package com.board.portfolio.security.handler;

import com.board.portfolio.exception.ApiError;
import com.board.portfolio.exception.GlobalErrorContent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SignInFilterFailureHandler implements AuthenticationFailureHandler {
    private final MessageSource validMessageSource;
    private final ResponseHandler responseHandler;
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        String message = validMessageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
        responseHandler.response400(res, new ApiError(HttpStatus.BAD_REQUEST, Arrays.asList(new GlobalErrorContent(message))));
    }


}
