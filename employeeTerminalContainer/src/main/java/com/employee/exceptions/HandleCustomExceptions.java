package com.employee.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class HandleCustomExceptions {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = {TakenUserNameException.class, NoRolesException.class})
    public ResponseEntity<Object> responseEntity(RuntimeException e) {

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionResponse);
    }

    @ExceptionHandler(value = {CustomSecurityException.class, ServletException.class, HttpClientErrorException.class})
    public ResponseEntity<Object> responseEntityForSecurity(HttpClientErrorException e) throws JsonProcessingException {

//        HttpStatusCode httpStatusCode = e.getHttpStatusCode();
//
//        SecurityExceptionResponse securityExceptionResponse = new SecurityExceptionResponse(
//                e.getMessage(),
//                HttpStatus.valueOf(httpStatusCode.value())
//        );
//
//        return ResponseEntity
//                .status(HttpStatusCode.valueOf(httpStatusCode.value()))
//                .headers(e.getHttpHeaders())
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(securityExceptionResponse);
//    }
        //HttpStatusCode httpStatusCode = e.getMessage();

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