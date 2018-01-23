package com.playtika.automation.feign.carsshop.service;

import com.playtika.automation.feign.carsshop.exception.CarOnSaleNotFoundException;
import com.playtika.automation.feign.carsshop.exception.InvalidFileContentException;
import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.exception.NoBestDealFoundException;
import com.playtika.automation.feign.carsshop.exception.NotAllRequiredParametersReceivedException;
import com.playtika.automation.feign.carsshop.facade.CarShopFacade;
import com.playtika.automation.feign.carsshop.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        service.addCars("/src");
    }

    @Test
    public void shouldReturnEmptyResultWhenFileIsEmpty() {
        assertThat(service.addCars("src/test/resources/files/empty.csv"), empty());
        verify(carShopFacade, never()).getCarReport(Matchers.any(CarSaleDetails.class));
    }

    @Test(expected = InvalidFileContentException.class)
    public void shouldReturnExceptionWhenDataIsCorruptedInFile() {
        service.addCars("src/test/resources/files/corrupted.csv");
        verify(carShopFacade, never()).getCarReport(Matchers.any(CarSaleDetails.class));
    }

    @Test
    public void shouldReturnCarReport() {
        CarReport expectedReport = generateCarReport();
        List<CarReport> expectedCarReport = Collections.singletonList(expectedReport);
        when(carShopFacade.getCarReport(expectedReport.getCarDetails())).thenReturn(expectedReport);
        assertThat(service.addCars("src/test/resources/files/carToAdd.csv"), equalTo(expectedCarReport));
        verify(carShopFacade).getCarReport(expectedReport.getCarDetails());
    }

    @Test
    public void shouldReturnDealInfoOnDealCreation() {
        DealInfo dealInfo = new DealInfo(1L, 1L);
        Customer customer = new Customer("Den", "1111");
        when(carShopFacade.createDeal(customer, 10000, 1L))
                .thenReturn(dealInfo);
        assertThat(service.createDeal(1L, 10000, customer), equalTo(dealInfo));
    }

    @Test(expected = CarOnSaleNotFoundException.class)
    public void shouldThrowExceptionWhenCarNotOnSAleOnDealCreation() {
        Customer customer = new Customer("Den", "1111");
        when(carShopFacade.createDeal(customer, 10000, 1L))
                .thenThrow(CarOnSaleNotFoundException.class);
        service.createDeal(1L, 10000, customer);
    }

    @Test(expected = NotAllRequiredParametersReceivedException.class)
    public void shouldThrowExceptionWhenNotAllParamsOnDealCreation() {
        Customer customer = new Customer("Den", "1111");
        when(carShopFacade.createDeal(customer, 10000, 1L))
                .thenThrow(NotAllRequiredParametersReceivedException.class);
        service.createDeal(1L, 10000, customer);
    }

    @Test
    public void shouldReturnDealInfoWhenBestDealFound() {
        DealInfo dealInfo = new DealInfo(1L, 1L);
        when(carShopFacade.findTheBestDeal(1L))
                .thenReturn(dealInfo);
        assertThat(service.findTheBestDeal(1L), equalTo(dealInfo));
    }

    @Test(expected = NoBestDealFoundException.class)
    public void shouldThrowExceptionWhenNoBestDealFound() {
        when(carShopFacade.findTheBestDeal(1L))
                .thenThrow(NoBestDealFoundException.class);
        service.findTheBestDeal(1L);
    }

    @Test
    public void shouldReturnTrueWhenDealAccepted() {
        when(carShopFacade.acceptDeal(1L)).thenReturn(true);
        assertTrue(service.acceptDeal(1L));
    }

    @Test
    public void shouldReturnFalseWhenDealIsNOtAccepted() {
        when(carShopFacade.acceptDeal(1L)).thenReturn(false);
        assertFalse(service.acceptDeal(1L));
    }

    @Test
    public void shoulDoNotThrowAnyExceptionWhenDealRejected() {
        doNothing().when(carShopFacade).rejectDeal(1L);
        service.acceptDeal(1L);
    }

    private CarReport generateCarReport() {
        Car car = new Car("DS123", "BMW", 2015, "green");
        SaleInfo carDetails = new SaleInfo(15000, "09689638521");
        CarSaleDetails carWithDetails = new CarSaleDetails(car, carDetails);
        return new CarReport(1L, carWithDetails, ReportStatus.ADDED);
    }


}