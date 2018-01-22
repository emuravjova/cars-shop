package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.exception.CarOnSaleNotFoundException;
import com.playtika.automation.feign.carsshop.exception.InvalidFileContent;
import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.exception.NoBestDealFoundException;
import com.playtika.automation.feign.carsshop.exception.NotAllRequiredParametersReceivedException;
import com.playtika.automation.feign.carsshop.model.*;
import com.playtika.automation.feign.carsshop.service.CarShopService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CarsShopController.class)
public class CarsShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarShopService carService;

    private final static String NUMBER = "AS123";
    private final static String BRAND = "BMW";
    private final static Integer YEAR = 2007;
    private final static String COLOR = "blue";
    private final static String FILE = "pathToFile";
    private final static Integer PRICE = 25000;
    private final static String CONTACTS = "Ron 0982345678";

    @Test
    public void shouldReturn200withReportOnAddCarsForSale() throws Exception {
        CarSaleDetails carWithDetails = new CarSaleDetails(new Car(NUMBER, BRAND, YEAR, COLOR), new SaleInfo(PRICE,CONTACTS));
        List<CarReport> expectedReport = Collections.singletonList(new CarReport(1L,carWithDetails, ReportStatus.ADDED));
        when(carService.addCars(FILE)).thenReturn(expectedReport);
        mockMvc.perform(post("/cars")
                .content(FILE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].carId").value(1L))
                .andExpect(jsonPath("$[0].carDetails.car.number").value(NUMBER))
                .andExpect(jsonPath("$[0].carDetails.car.brand").value(BRAND))
                .andExpect(jsonPath("$[0].carDetails.car.year").value(YEAR))
                .andExpect(jsonPath("$[0].carDetails.car.color").value(COLOR))
                .andExpect(jsonPath("$[0].carDetails.saleInfo.price").value(PRICE))
                .andExpect(jsonPath("$[0].carDetails.saleInfo.contacts").value(CONTACTS))
                .andExpect(jsonPath("$[0].status").value("ADDED"));
    }

    @Test
    public void shouldReturn400BadRequestOnAddCarsWhenFileFotFound() throws Exception {
        when(carService.addCars("")).thenThrow(InvalidFileException.class);
        mockMvc.perform(post("/cars")
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400BadRequestOnAddCarsWhenFileIsCurrupted() throws Exception {
        when(carService.addCars("")).thenThrow(InvalidFileContent.class);
        mockMvc.perform(post("/cars")
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenCarIdPriceAreAbsentOnCreateDeal() throws Exception {
        postDeal(null,null,createCustomerInJson("Den","1111"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenPriceAreEmptyOnCreateDeal() throws Exception {
        postDeal("","",createCustomerInJson("Den","1111"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenCustomerIsEmptyOnCreateDeal() throws Exception {
        postDeal(String.valueOf(20000),String.valueOf(1L),createCustomerInJson("",""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenNoCustomerProvidedOnCreateDeal() throws Exception {
        mockMvc.perform(post("/deal")
                .param("price", String.valueOf(20000))
                .param("id", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenNotAllParametersException() throws Exception {
        Customer customer = new Customer("Den","1111");
        when(carService.createDeal(1L, 10000, customer))
                .thenThrow(new NotAllRequiredParametersReceivedException("message"));
        postDeal(String.valueOf(10000), String.valueOf(1L),createCustomerInJson(customer.getName(),customer.getContacts()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200whenDealWasCreated() throws Exception {
        DealInfo dealInfo = new DealInfo(1L, 1L);
        Customer customer = new Customer("Den","1111");
        when(carService.createDeal(1L, 10000, customer))
                .thenReturn(dealInfo);
        postDeal(String.valueOf(10000), String.valueOf(1L),createCustomerInJson(customer.getName(),customer.getContacts()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.offerId").value(1L));
    }

    @Test
    public void shouldReturn404whenCarIsNotOnSale() throws Exception {
        Customer customer = new Customer("Den","1111");
        when(carService.createDeal(1L, 1000, customer))
                .thenThrow(new CarOnSaleNotFoundException("message"));
        postDeal(String.valueOf(1000), String.valueOf(1L),createCustomerInJson(customer.getName(),customer.getContacts()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200whenBestDealIsFound() throws Exception {
        DealInfo dealInfo = new DealInfo(1L, 1L);
        when(carService.findTheBestDeal(1L))
                .thenReturn(dealInfo);
        mockMvc.perform(get("/offer/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.offerId").value(1L));
    }

    @Test
    public void shouldReturn404whenBestDealNotFound() throws Exception {
        when(carService.findTheBestDeal(1L))
                .thenThrow(new NoBestDealFoundException("message"));
        mockMvc.perform(get("/offer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200whenDealIsAccepted() throws Exception {
        when(carService.acceptDeal(1L))
                .thenReturn(true);
        mockMvc.perform(put("/acceptDeal/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn409whenDealCouldNotBeAccepted() throws Exception {
        when(carService.acceptDeal(1L))
                .thenReturn(false);
        mockMvc.perform(put("/acceptDeal/1"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturn200whenDealIsRejected() throws Exception {
        mockMvc.perform(put("/rejectDeal/1"))
                .andExpect(status().isOk());
    }

    private static String createCustomerInJson(String name, String contacts) {
        return String.format("{ \"name\": \"%s\", \"contacts\":\"%s\"}", name, contacts);

    }

    private ResultActions postDeal(String price, String id, String body) throws Exception {
        return mockMvc.perform(post("/deal")
                .param("price", price)
                .param("carId", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));
    }
}
