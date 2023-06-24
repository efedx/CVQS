package com.example.config;

import com.example.exceptions.ExceptionResponse;
import com.example.filters.ExceptionHandlerFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.filters.ExceptionHandlerFilter.convertObjectToJson;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        //response.sendError(403, "Access Denied");

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                "No authentication",
                HttpStatus.UNAUTHORIZED);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("access_denied_reason", "authentication_required");
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(exceptionResponse));

    }
}