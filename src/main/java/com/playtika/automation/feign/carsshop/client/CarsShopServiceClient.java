package com.playtika.automation.feign.carsshop.client;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.AddNewCarResponse;
import com.playtika.automation.feign.carsshop.model.Customer;
import com.playtika.automation.feign.carsshop.model.DealInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "car-shop")
public interface CarsShopServiceClient {

    @PostMapping("/cars")
    AddNewCarResponse addCars(@RequestBody Car car,
                              @RequestParam("price") int price,
                              @RequestParam("contacts") String contacts);

    @PostMapping("/deal")
    DealInfo createDeal (
            @RequestBody Customer customer,
            @RequestParam("price") int price,
            @RequestParam("carId") Long id);

    @GetMapping("/offer/{id}")
    DealInfo findTheBestDeal (@PathVariable("id") long id);

    @PutMapping("/acceptDeal/{id}")
    ResponseEntity<Void> acceptDeal (@PathVariable("id") long id);

    @PutMapping(value = "/rejectDeal/{id}")
    void rejectDeal (@PathVariable("id") long id);
}
