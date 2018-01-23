package com.playtika.automation.feign.carsshop.exception;

public class InvalidFileContentException extends RuntimeException {
    public InvalidFileContentException(String message) {
        super(message);
    }
}
