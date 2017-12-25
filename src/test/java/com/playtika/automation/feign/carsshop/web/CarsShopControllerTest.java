package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.model.*;
import com.playtika.automation.feign.carsshop.service.CarShopService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        List<CarReport> expectedReport = Collections.singletonList(new CarReport(carWithDetails, ReportStatus.ADDED));
        when(carService.addCar(FILE)).thenReturn(expectedReport);
        mockMvc.perform(post("/cars")
                .content(FILE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
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
        when(carService.addCar("")).thenThrow(InvalidFileException.class);
        mockMvc.perform(post("/cars")
                .content(""))
                .andExpect(status().isBadRequest());
    }
}
