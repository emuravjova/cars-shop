package com.playtika.automation.feign.carsshop.client;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.AddNewCarResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "car-shop")
public interface CarsShopServiceClient {

    @PostMapping("/cars")
    AddNewCarResponse addCar(@RequestBody Car car,
                             @RequestParam("price") int price,
                             @RequestParam("contacts") String contacts);
}
