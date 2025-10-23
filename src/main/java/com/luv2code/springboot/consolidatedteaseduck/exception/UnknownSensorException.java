package com.luv2code.springboot.consolidatedteaseduck.exception;

public class UnknownSensorException extends RuntimeException {
    public UnknownSensorException(String message) {
        super(message);
    }
}
