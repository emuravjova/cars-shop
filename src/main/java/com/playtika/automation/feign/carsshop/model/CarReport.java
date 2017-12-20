package com.playtika.automation.feign.carsshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Created by emuravjova on 12/19/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarReport {
    private Car car;
    private String message;
}
