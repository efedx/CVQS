package com.defect.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;



@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HandleCustomExceptions {
    @ExceptionHandler(value = {NoDefectWithIdException.class, NoVehicleWithIdException.class})
    public ResponseEntity<Object> responseEntity(RuntimeException e) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());

        return ResponseEntity
                .status(400)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionResponse);
    }

}
