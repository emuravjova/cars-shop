package com.playtika.automation.feign.carsshop.exception;

/**
 * Created by emuravjova on 1/22/2018.
 */
public class CarOnSaleNotFoundException extends RuntimeException {
    public CarOnSaleNotFoundException(String message) {
        super(message);
    }
}
