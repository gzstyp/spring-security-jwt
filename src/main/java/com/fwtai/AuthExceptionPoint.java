package com.fwtai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthExceptionPoint implements AuthenticationEntryPoint{

    public void commence(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException exception) throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String reason = "统一处理，原因：访问此资源需要完全身份验证," + exception.getMessage();
        response.getWriter().write(new ObjectMapper().writeValueAsString(reason));
    }
}