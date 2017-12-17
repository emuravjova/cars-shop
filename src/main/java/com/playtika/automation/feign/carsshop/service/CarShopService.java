package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarId;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;

import java.util.List;

public interface CarShopService {
    List<CarSaleDetails> getAllCars();
    SaleInfo getCarDetailsById(long id);
    void deleteCarById(long id);
    CarId addCar(Car car, int price, String contacts);
}
