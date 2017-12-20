package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;
import com.playtika.automation.feign.carsshop.service.CarShopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by emuravjova on 12/19/2017.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cars")
@Slf4j
public class CarsShopController {

    private final CarShopService carService;
    private static final String SEPARATOR = ",";

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarReport> addCar(@RequestBody String fileName) {
        List<CarSaleDetails> carsToAdd = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            carsToAdd = br.lines()
                    .map(CarsShopController::mapToCarSaleDetails)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MyEx;
        }

        return carService.addCar(carsToAdd);
    }

    private static CarSaleDetails mapToCarSaleDetails (String line) {
        String[] p = line.split(SEPARATOR);
        return new CarSaleDetails(new Car(p[0],p[1], Integer.parseInt(p[2]),p[3]), new SaleInfo(Integer.parseInt(p[4]),p[5]));
    }

    @ExceptionHandler
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleMyEx(MyEx e){
        log.error("ljdfsg {}", e);
        return "jfgvsjvhg";
    }
}
