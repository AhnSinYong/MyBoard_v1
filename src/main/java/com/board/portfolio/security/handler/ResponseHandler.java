package com.board.portfolio.security.handler;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.board.portfolio.util.StaticUtils.objectMapper;

@Component
public class ResponseHandler {
    public void response400(HttpServletResponse res, Object object) throws IOException {
        res.setStatus(400);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=UTF-8");
        String json = objectMapper.writeValueAsString(object);
        PrintWriter out = res.getWriter();
        out.print(json);
    }
}
