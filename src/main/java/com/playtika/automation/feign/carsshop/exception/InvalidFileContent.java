package com.playtika.automation.feign.carsshop.exception;

public class InvalidFileContent extends RuntimeException{
    public InvalidFileContent(String message) {
        super(message);
    }
}
