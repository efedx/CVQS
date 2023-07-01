package com.example.exceptions;

import com.example.exceptions.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

//        ExceptionResponse exceptionResponse = new ExceptionResponse(
//                "No authentication");
//
//        String exceptionResponseString = objectMapper.writeValueAsString(exceptionResponse);
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.addHeader("access_denied_reason", "authentication_required");
//        response.setContentType("application/json");
//        response.getWriter().write(exceptionResponseString);

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No authentication");
    }
}