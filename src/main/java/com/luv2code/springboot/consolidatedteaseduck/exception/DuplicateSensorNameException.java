package com.luv2code.springboot.consolidatedteaseduck.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateSensorNameException extends RuntimeException {

    public DuplicateSensorNameException(){}

    public DuplicateSensorNameException(String message) {
        super(message);
    }
}
