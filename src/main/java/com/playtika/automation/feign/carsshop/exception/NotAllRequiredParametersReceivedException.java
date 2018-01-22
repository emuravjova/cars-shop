package com.playtika.automation.feign.carsshop.exception;

/**
 * Created by emuravjova on 1/22/2018.
 */
public class NotAllRequiredParametersReceivedException extends RuntimeException {
    public NotAllRequiredParametersReceivedException(String message) {
        super(message);
    }
}
