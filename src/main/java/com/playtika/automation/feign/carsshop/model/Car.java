package com.playtika.automation.feign.carsshop.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Car {
    private String number;
    private String brand;
    private Integer year;
    private String color;
}
