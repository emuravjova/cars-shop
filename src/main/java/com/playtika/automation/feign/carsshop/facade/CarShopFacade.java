package com.playtika.automation.feign.carsshop.facade;

import com.playtika.automation.feign.carsshop.client.CarsShopServiceClient;
import com.playtika.automation.feign.carsshop.model.*;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by emuravjova on 12/22/2017.
 */
@Slf4j
@AllArgsConstructor
@Component
public class CarShopFacade {

    private CarsShopServiceClient carsShopServiceClient;

    public CarReport getCarReport(CarSaleDetails carToAdd) {
        ReportStatus reportStatus;
        Long id=0L;
        Car car = carToAdd.getCar();
        int price = carToAdd.getSaleInfo().getPrice();
        String contacts = carToAdd.getSaleInfo().getContacts();
        try {
            id = carsShopServiceClient.addCars(car, price, contacts).getId();
            reportStatus = ReportStatus.ADDED;
        } catch (FeignException e) {
            int statusCode = e.status();
            switch (statusCode) {
                case 400:
                    reportStatus = ReportStatus.NOT_ALL_PARAMETERS;
                    break;
                case 500:
                    reportStatus = ReportStatus.ALREADY_ON_SALE;
                    break;
                default:
                    reportStatus = ReportStatus.UNKNOWN;
            }
        }
        return new CarReport(id, carToAdd, reportStatus);
    }

    public DealInfo createDeal(Customer customer, int price, Long id) {
        DealInfo dealInfo = new DealInfo();
        try {
            dealInfo = carsShopServiceClient.createDeal(customer, price, id);
        } catch (FeignException e) {
            int statusCode = e.status();
            switch (statusCode) {
                case 404:
                    throw new CarOnSaleNotFoundException("Such car is not on sale or no such car at all!");
                    break;
                case 400:
                    throw new NotAllRequiredParametersReceived("Not all parameters!");
                    break;
                default:
                    reportStatus = ReportStatus.UNKNOWN;
            }
        }
        return dealInfo;
    }
}
