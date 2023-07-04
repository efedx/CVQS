package com.employee.exceptions;

public class NoEmployeeWithIdException extends RuntimeException{
    public NoEmployeeWithIdException(String message) {
        super(message);
    }
}
