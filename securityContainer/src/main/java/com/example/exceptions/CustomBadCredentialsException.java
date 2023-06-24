package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomBadCredentialsException extends RuntimeException {

    public CustomBadCredentialsException(String message) {
        super(message);
    }

    public CustomBadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
