package com.playtika.automation.feign.carsshop.client;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarId;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "car-shop", url = "http://localhost:8082")
public interface CarsShopFeign {

    @RequestMapping(method = RequestMethod.POST, value = "/cars")
    CarId addCar(@RequestBody Car car,
                        @RequestParam("price") int price,
                        @RequestParam("contacts") String contacts);
}
