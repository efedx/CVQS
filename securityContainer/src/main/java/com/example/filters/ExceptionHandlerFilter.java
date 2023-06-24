package com.example.filters;

import com.example.exceptions.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException | ServletException e) {

            HttpStatus badRequest = HttpStatus.BAD_REQUEST;

            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    e.getMessage(),
                    badRequest);

            response.setStatus(badRequest.value());
            response.setContentType("application/json");
            response.getWriter().write(convertObjectToJson(exceptionResponse));
        }
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
