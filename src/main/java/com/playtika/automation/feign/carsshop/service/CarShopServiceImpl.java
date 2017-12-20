package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import com.playtika.automation.feign.carsshop.model.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarShopServiceImpl implements CarShopService {

    @Autowired
    private CarsShopFeign carsShopFeign;

    @Override
    public List<CarReport> addCar(List<CarSaleDetails> carsToAdd) {
        List<CarReport> carReports = new ArrayList<>();
        carReports = carsToAdd.stream().map(this::getCarReport).collect(Collectors.toList());
        return carReports;
    }

    private CarReport getCarReport(CarSaleDetails carToAdd) {
        String message;
        CarReport report = new CarReport();
        try {
            CarId id = carsShopFeign.addCar(carToAdd.getCar(), carToAdd.getSaleInfo().getPrice(), carToAdd.getSaleInfo().getContacts());
            message = "Car" + carToAdd.getCar().toString() + "has been added for sale with id=" + String.valueOf(id.getId());
        } catch (FeignException e) {
            switch (e.status()) {
                case 400:
                    message = "String1";
                    break;
                case 500:
                    message = "String2";
                    break;
                default:
                    message = "Unknoun" + e.status();
            }
        }
        report.setCar(carToAdd.getCar());
        report.setMessage(message);
        return report;
    }
}
