package com.playtika.automation.feign.carsshop.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@Value
public class Car {
    @ApiModelProperty(notes = "Car plate number", required = true, example = "QW123")
    String number;
    @ApiModelProperty(notes = "Car brand", required = true, example = "BMW")
    String brand;
    @ApiModelProperty(notes = "Car manufactured year", required = true, example = "2017")
    Integer year;
    @ApiModelProperty(notes = "Car color", required = true, example = "green")
    String color;
}
