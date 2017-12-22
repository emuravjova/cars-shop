package com.playtika.automation.feign.carsshop.facade;

import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import com.playtika.automation.feign.carsshop.model.CarId;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by emuravjova on 12/22/2017.
 */
@Slf4j
@AllArgsConstructor
@Component
public class CarShopFacade {

    @Autowired
    private CarsShopFeign carsShopFeign;

    public CarReport getCarReport(CarSaleDetails carToAdd) {
        String message;
        CarReport report = new CarReport();
        try {
            CarId id = carsShopFeign.addCar(carToAdd.getCar(), carToAdd.getSaleInfo().getPrice(), carToAdd.getSaleInfo().getContacts());
            message = carToAdd.getCar().toString() + "has been added for sale with id=" + String.valueOf(id.getId());
        } catch (FeignException e) {
            switch (e.status()) {
                case 400:
                    message = "Car has not been added due to not all required parameters was received";
                    break;
                case 500:
                    message = "Car has not been added due to such car is already on sale";
                    break;
                default:
                    message = "Unknown error, status code = " + e.status();
            }
        }
        report.setCar(carToAdd.getCar());
        report.setMessage(message);
        return report;
    }
}
