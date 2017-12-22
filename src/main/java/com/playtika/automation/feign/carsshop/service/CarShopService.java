package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.exception.FileProblemException;
import com.playtika.automation.feign.carsshop.model.*;

import java.util.List;

public interface CarShopService {
    List<CarReport> addCar(String fileName) throws FileProblemException;
}
