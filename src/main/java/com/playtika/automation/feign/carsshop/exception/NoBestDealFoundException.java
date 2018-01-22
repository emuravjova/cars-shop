package com.playtika.automation.feign.carsshop.exception;

/**
 * Created by emuravjova on 1/22/2018.
 */
public class NoBestDealFoundException extends RuntimeException {
    public NoBestDealFoundException(String message) {
        super(message);
    }
}
