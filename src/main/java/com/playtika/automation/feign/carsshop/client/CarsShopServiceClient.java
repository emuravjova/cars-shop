package com.playtika.automation.feign.carsshop.client;

import com.playtika.automation.feign.carsshop.model.AddNewCarResponse;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.Customer;
import com.playtika.automation.feign.carsshop.model.DealInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "car-shop")
public interface CarsShopServiceClient {

    @PostMapping("/cars")
    AddNewCarResponse addCars(@RequestBody Car car,
                              @RequestParam("price") int price,
                              @RequestParam("contacts") String contacts);

    @PostMapping("/deal")
    DealInfo createDeal(
            @RequestBody Customer customer,
            @RequestParam("price") int price,
            @RequestParam("carId") Long id);

    @GetMapping("/offer/{id}")
    DealInfo findTheBestDeal(@PathVariable("id") long id);

    @PutMapping("/acceptDeal/{id}")
    ResponseEntity<Void> acceptDeal(@PathVariable("id") long id);

    @PutMapping(value = "/rejectDeal/{id}")
    void rejectDeal(@PathVariable("id") long id);
}
