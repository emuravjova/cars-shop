package com.playtika.automation.feign.carsshop.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SaleInfo {
    private int price;
    private String contacts;
}
