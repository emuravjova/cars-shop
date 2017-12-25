package com.playtika.automation.feign.carsshop.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@Value
public class SaleInfo {
    @ApiModelProperty(notes = "Car price", required = true, example = "14000")
    int price;
    @ApiModelProperty(notes = "Seller contacts", required = true, example = "0985673456")
    String contacts;
}
