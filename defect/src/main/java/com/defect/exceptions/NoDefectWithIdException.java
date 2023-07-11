package com.defect.exceptions;

public class NoDefectWithIdException extends RuntimeException {
    public NoDefectWithIdException(String message) {
        super(message);
    }
}
