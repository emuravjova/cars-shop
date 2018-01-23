package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.exception.InvalidFileContentException;
import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.facade.CarShopFacade;
import com.playtika.automation.feign.carsshop.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarShopServiceImpl implements CarShopService {

    private static final String SEPARATOR = ",";

    private CarShopFacade carsShopFacade;

    public CarShopServiceImpl(CarShopFacade carsShopFacade) {
        this.carsShopFacade = carsShopFacade;
    }

    @Override
    public List<CarReport> addCars(String fileName) {
        return readCarDetailsFromFile(fileName).stream()
                .map(carsShopFacade::getCarReport)
                .collect(Collectors.toList());
    }

    @Override
    public DealInfo createDeal(Long id, int price, Customer customer) {
        return carsShopFacade.createDeal(customer, price, id);
    }

    @Override
    public DealInfo findTheBestDeal(Long id) {
        return carsShopFacade.findTheBestDeal(id);
    }

    @Override
    public boolean acceptDeal(long id) {
        return carsShopFacade.acceptDeal(id);
    }

    @Override
    public void rejectDeal(long id) {
        carsShopFacade.rejectDeal(id);
    }

    private List<CarSaleDetails> readCarDetailsFromFile(String fileName) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            return br.lines()
                    .map(this::mapToCarSaleDetails)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new InvalidFileException("Can not handle file: " + fileName);
        }
    }

    private CarSaleDetails mapToCarSaleDetails(String line) {
        String[] p = line.split(SEPARATOR);
        if (p.length < 6) {
            throw new InvalidFileContentException("Incorrect file content in line: " + line);
        }
        Car car = getCar(p);
        SaleInfo saleInfo = getSaleInfo(p);
        return new CarSaleDetails(car, saleInfo);
    }

    private Car getCar(String[] p) {
        String number = p[0];
        String brand = p[1];
        int year = Integer.parseInt(p[2]);
        String color = p[3];
        return new Car(number, brand, year, color);
    }

    private SaleInfo getSaleInfo(String[] p) {
        int price = Integer.parseInt(p[4]);
        String contacts = p[5];
        return new SaleInfo(price, contacts);
    }
}
