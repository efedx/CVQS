package com.example.exceptions;

public class NoDefectWithIdException extends RuntimeException {
    public NoDefectWithIdException(String message) {
        super(message);
    }

    public NoDefectWithIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
