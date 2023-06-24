package com.example.config;

import com.example.exceptions.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.filters.ExceptionHandlerFilter.convertObjectToJson;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Access Denied");

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                "No authorization",
                HttpStatus.UNAUTHORIZED);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("access_denied_reason", "authentication_required");
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(exceptionResponse));
    }
}