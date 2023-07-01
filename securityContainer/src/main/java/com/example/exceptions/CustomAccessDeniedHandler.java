package com.example.exceptions;

import com.example.exceptions.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {

//        ExceptionResponse exceptionResponse = new ExceptionResponse("No authorization");
//
//        String exceptionResponseAsString = objectMapper.writeValueAsString(exceptionResponse);
//        String test = objectMapper.convertValue(exceptionResponse, String.class);
//
//       // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.addHeader("access_denied_reason", "authentication_required");
//        response.setContentType("application/json");  // Set content type of the response so that jQuery knows what it can expect.
//        response.setContentLength(exceptionResponseAsString.length());
//        response.getWriter().write(exceptionResponseAsString);
//        response.flushBuffer();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No authorization");
    }
}