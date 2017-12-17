package com.playtika.automation.feign.carsshop;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarId;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;
import com.playtika.automation.feign.carsshop.service.CarShopServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
public class CarsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsShopApplication.class, args);

		CarShopServiceImpl carRerository = new CarShopServiceImpl();
		Car car = new Car("AEW123", "BMW", 2016, "red");
		CarId id = carRerository.addCar(car, 1200, "0979876543");
		log.info("Car {} with id {} has been added for selling", car, id);
		SaleInfo carInfo = carRerository.getCarDetailsById(id.getId());
		log.info("Car {} has been added for selling with following details {}", car, carInfo);
		List<CarSaleDetails> allCars = carRerository.getAllCars();
		log.info("All available cars: {}", allCars);
		carRerository.deleteCarById(id.getId());
		allCars = carRerository.getAllCars();
		log.info("All available cars after car removing: {}", allCars);
	}
}
