package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.Customer;
import com.playtika.automation.feign.carsshop.model.DealInfo;

import java.util.List;

public interface CarShopService {
    List<CarReport> addCars(String fileName);

    DealInfo createDeal(Long id, int price, Customer customer);

    DealInfo findTheBestDeal(Long id);

    boolean acceptDeal(long id);

    void rejectDeal(long id);
}
