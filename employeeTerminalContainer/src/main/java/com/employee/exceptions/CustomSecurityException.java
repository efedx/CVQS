package com.employee.exceptions;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomSecurityException extends RuntimeException{
    private final HttpStatusCode httpStatusCode;
    private final HttpHeaders httpHeaders;

    public CustomSecurityException(String message, HttpStatusCode httpStatusCode, HttpHeaders httpHeaders) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.httpHeaders = httpHeaders;
    }
}
