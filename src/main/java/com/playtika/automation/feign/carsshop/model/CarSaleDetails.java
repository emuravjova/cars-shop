package com.playtika.automation.feign.carsshop.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CarSaleDetails {
    Car car;
    SaleInfo saleInfo;
}
