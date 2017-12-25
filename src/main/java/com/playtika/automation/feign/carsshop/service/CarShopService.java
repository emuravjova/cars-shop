package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.model.CarReport;

import java.util.List;

public interface CarShopService {
    List<CarReport> addCar(String fileName);
}
