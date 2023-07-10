package com.gateway.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
@RequiredArgsConstructor
public class HandleCustomExceptions {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<Object> responseEntityForSecurity(HttpClientErrorException e) throws JsonProcessingException {

        if(!e.getResponseBodyAsString().isEmpty()) {
            SecurityExceptionResponse securityExceptionResponse = objectMapper.readValue(e.getResponseBodyAsString(), SecurityExceptionResponse.class);
            return ResponseEntity
                    .status(e.getStatusCode())
                    .headers(HttpHeaders.EMPTY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(securityExceptionResponse);
        }
        else {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .headers(HttpHeaders.EMPTY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }
        }

}