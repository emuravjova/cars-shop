package com.playtika.automation.feign.carsshop.facade;

import com.playtika.automation.feign.carsshop.client.CarsShopServiceClient;
import com.playtika.automation.feign.carsshop.exception.CarOnSaleNotFoundException;
import com.playtika.automation.feign.carsshop.exception.NoBestDealFoundException;
import com.playtika.automation.feign.carsshop.exception.NotAllRequiredParametersReceivedException;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.Customer;
import com.playtika.automation.feign.carsshop.model.DealInfo;
import com.playtika.automation.feign.carsshop.model.ReportStatus;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        DealInfo dealInfo;
        try {
            dealInfo = carsShopServiceClient.createDeal(customer, price, id);
        } catch (FeignException e) {
            int statusCode = e.status();
            switch (statusCode) {
                case 404:
                    throw new CarOnSaleNotFoundException("Such car is not on sale or no such car at all");
                case 400:
                    throw new NotAllRequiredParametersReceivedException("Not all parameters received");
                default:
                    throw new RuntimeException("Unable to create the deal");
            }
        }
        return dealInfo;
    }

    public DealInfo findTheBestDeal(Long id){
        DealInfo dealInfo;
        try {
            dealInfo = carsShopServiceClient.findTheBestDeal(id);
        } catch (FeignException e){
            int statusCode = e.status();
            switch (statusCode){
                case 404:
                    throw new NoBestDealFoundException("No best deal found for open offer");
                default:
                    throw new RuntimeException("Unable to find the best deal");
            }
        }
        return dealInfo;
    }

    public boolean acceptDeal(Long id){
        try {
            carsShopServiceClient.acceptDeal(id);
        } catch (FeignException e){
            int statusCode = e.status();
            switch (statusCode){
                case 409:
                    return false;
                default:
                    throw new RuntimeException("Unable to accept deal");
            }
        }
        return true;
    }

    public void rejectDeal(Long id){
        try {
            carsShopServiceClient.rejectDeal(id);
        } catch (FeignException e){
            throw new RuntimeException("Unable to reject deal");
        }
    }

}
