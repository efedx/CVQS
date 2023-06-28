package com.example.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomSecurityException extends RuntimeException{
    private final HttpStatusCode httpStatusCode;

    public CustomSecurityException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
