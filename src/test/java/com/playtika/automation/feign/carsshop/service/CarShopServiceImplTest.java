package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.exception.InvalidFileContent;
import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.facade.CarShopFacade;
import com.playtika.automation.feign.carsshop.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CarShopServiceImplTest {

    private CarShopService service;

    @Mock
    private CarShopFacade carShopFacade;

    @Before
    public void init() {
        service = new CarShopServiceImpl(carShopFacade);
    }

    @Test(expected = InvalidFileException.class)
    public void shouldReturnExceptionWhenNoFileFound() {
        service.addCar("/src");
    }

    @Test
    public void shouldReturnEmptyResultWhenFileIsEmpty(){
        assertThat(service.addCar("src/test/resources/files/empty.csv"), empty());
        verify(carShopFacade, never()).getCarReport(Matchers.any(CarSaleDetails.class));
    }

    @Test(expected = InvalidFileContent.class)
    public void shouldReturnExceptionWhenDataIsCorruptedInFile() {
        service.addCar("src/test/resources/files/corrupted.csv");
        verify(carShopFacade, never()).getCarReport(Matchers.any(CarSaleDetails.class));
    }

    @Test
    public void shouldReturnCarReport(){
        CarReport expectedReport = generateCarReport();
        List<CarReport> expectedCarReport = Collections.singletonList(expectedReport);
        when(carShopFacade.getCarReport(expectedReport.getCarDetails())).thenReturn(expectedReport);
        assertThat(service.addCar("src/test/resources/files/carToAdd.csv"), equalTo(expectedCarReport));
        verify(carShopFacade).getCarReport(expectedReport.getCarDetails());
    }

    private CarReport generateCarReport() {
        Car car = new Car("DS123", "BMW",2015, "green");
        SaleInfo carDetails = new SaleInfo(15000, "09689638521");
        CarSaleDetails carWithDetails = new CarSaleDetails(car,carDetails);
        return new CarReport(1L, carWithDetails, ReportStatus.ADDED);
    }
}