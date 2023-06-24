package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;

public class SecurityException extends RuntimeException {

    HttpStatusCode httpStatusCode;

    public SecurityException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

//    public SecurityException(String message, Throwable cause, HttpStatus httpStatus) {
//        super(message, cause);
//        this.httpStatus = httpStatus;
//    }
}
