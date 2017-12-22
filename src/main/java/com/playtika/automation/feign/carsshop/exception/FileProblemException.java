package com.playtika.automation.feign.carsshop.exception;

import java.io.IOException;

/**
 * Created by emuravjova on 12/22/2017.
 */
public class FileProblemException extends IOException{
    public FileProblemException(String message) {
        super(message);
    }
}
