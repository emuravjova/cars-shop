package com.playtika.automation.feign.carsshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude={"carId"})
public class CarSaleDetails {
    private Long carId;
    private Car car;
    private SaleInfo saleInfo;
}
