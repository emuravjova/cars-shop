package com.playtika.automation.feign.carsshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarSaleDetails {
    private Car car;
    private SaleInfo saleInfo;
}
