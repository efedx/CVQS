package com.example.exceptions;

public class NoEmployeeWithIdException extends RuntimeException{
    public NoEmployeeWithIdException(String message) {
        super(message);
    }
}
