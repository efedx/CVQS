package com.example.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class SecurityExceptionResponse {
    private final String message;
    private final HttpStatus httpStatus;
}
