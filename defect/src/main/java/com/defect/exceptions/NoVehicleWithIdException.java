package com.defect.exceptions;

public class NoVehicleWithIdException extends RuntimeException {
    public NoVehicleWithIdException(String message) {
        super(message);
    }
}
