package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarId;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.util.List;

public class CarShopServiceImpl implements CarShopService{
    private static final String URI_CARS = "http://localhost:8082";

    private CarsShopFeign carClient = Feign.builder()
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .target(CarsShopFeign.class, URI_CARS);

    @Override
    public List<CarSaleDetails> getAllCars() {
        return carClient.getAllCars();
    }

    @Override
    public SaleInfo getCarDetailsById(long id) {
        return carClient.getCarDetailsById(id);
    }

    @Override
    public void deleteCarById(long id) {
        carClient.deleteCarById(id);
    }

    @Override
    public CarId addCar(Car car, int price, String contacts) {
        return carClient.addCar(car, price, contacts);
    }
}
