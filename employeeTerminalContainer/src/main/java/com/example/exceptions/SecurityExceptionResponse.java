package com.example.exceptions;

import org.springframework.http.HttpStatus;

public record SecurityExceptionResponse(String message, HttpStatus httpStatus) {
}
