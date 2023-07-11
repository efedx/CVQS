package com.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HandleCustomExceptions {
    @ExceptionHandler(value = {CustomBadCredentialsException.class, CustomUsernameNotFoundException.class})
    public ResponseEntity<Object> responseEntity(RuntimeException e) {

        HttpStatus badRequestHttStatus = HttpStatus.BAD_REQUEST;

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage()
        );

        return ResponseEntity
                .status(badRequestHttStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionResponse);
    }
}
