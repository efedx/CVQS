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
    @ExceptionHandler(value = {NoDefectWithIdException.class})
    public ResponseEntity<Object> responseEntity(RuntimeException e) {

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        //return new ResponseEntity<>(exceptionResponse, badRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(exceptionResponse);
    }

}
