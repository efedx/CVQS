package com.employee.exceptions;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomSecurityException extends RuntimeException{

    public CustomSecurityException(String message) {
        super(message);

    }
}
