package com.playtika.automation.feign.carsshop.client;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarId;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@Headers("Accept: application/json")
public interface CarsShopFeign {

    @Headers("Content-Type: application/json")
    @RequestLine("GET /cars")
    public List<CarSaleDetails> getAllCars();

    @Headers("Content-Type: application/json")
    @RequestLine("GET /cars/{id}")
    public SaleInfo getCarDetailsById(@Param("id") Long id);

    @RequestLine("DELETE /cars/{id}")
    public void deleteCarById(@Param("id") Long id);

    @Headers("Content-Type: application/json")
    @RequestLine("POST /cars/?price={price}&contacts={contacts}")
    public CarId addCar(Car car,
                        @Param("price") int price,
                        @Param("contacts") String contacts);
}
