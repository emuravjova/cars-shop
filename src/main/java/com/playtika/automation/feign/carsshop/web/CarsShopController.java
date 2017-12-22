package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.exception.FileProblemException;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;
import com.playtika.automation.feign.carsshop.service.CarShopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CarShopService carService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarReport> addCar(@RequestBody String fileName) throws FileProblemException {
        return carService.addCar(fileName);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleMyEx(FileProblemException e) {
        log.error("Error while file handling {}", e);
        return e.getMessage();
    }
}
