package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import com.playtika.automation.feign.carsshop.exception.FileProblemException;
import com.playtika.automation.feign.carsshop.facade.CarShopFacade;
import com.playtika.automation.feign.carsshop.model.*;
import com.playtika.automation.feign.carsshop.web.CarsShopController;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarShopServiceImpl implements CarShopService {

    @Autowired
    private CarShopFacade carsShopFacade;

    private static final String SEPARATOR = ",";

    @Override
    public List<CarReport> addCar(String fileName) throws FileProblemException {
        List<CarSaleDetails> carsToAdd;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            carsToAdd = br.lines()
                    .map(this::mapToCarSaleDetails)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileProblemException("Can not handle file: " + fileName);
        }
        List<CarReport> carReports;
        carReports = carsToAdd.stream()
                .map(carsShopFacade::getCarReport)
                .collect(Collectors.toList());
        return carReports;
    }

    private CarSaleDetails mapToCarSaleDetails(String line) {
        String[] p = line.split(SEPARATOR);
        return new CarSaleDetails(new Car(p[0], p[1], Integer.parseInt(p[2]), p[3]), new SaleInfo(Integer.parseInt(p[4]), p[5]));
    }
}
