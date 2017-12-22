package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.exception.FileProblemException;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarReport;
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
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CarsShopController.class)
public class CarsShopControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarShopService carService;

    private final static String NUMBER = "AS123";
    private final static String BRAND = "BMW";
    private final static Integer YEAR = 2007;
    private final static String COLOR = "blue";
    private final static String MESSAGE = "Car(number=AEW123, brand=BMW, year=2016, color=red)has been added for sale with id=1";
    private final static String FILE = "src/main/resources/cars/CarsToAdd.csv";

    @Test
    public void shouldReturn200withReportOnAddCarsForSale() throws Exception {
        Car car = new Car(NUMBER, BRAND, YEAR, COLOR);
        List<CarReport> expectedReport = Collections.singletonList(new CarReport(car, MESSAGE));
        when(carService.addCar(FILE)).thenReturn(expectedReport);
        mockMvc.perform(post("/cars")
                .content(FILE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].car.number").value(NUMBER))
                .andExpect(jsonPath("$[0].car.brand").value(BRAND))
                .andExpect(jsonPath("$[0].car.year").value(YEAR))
                .andExpect(jsonPath("$[0].car.color").value(COLOR))
                .andExpect(jsonPath("$[0].message").value(MESSAGE));
    }

    @Test
    public void shouldReturn400BadRequestOnAddCarsWhenFileFotFound() throws Exception {
        when(carService.addCar("")).thenThrow(FileProblemException.class);
        mockMvc.perform(post("/cars")
                .content(""))
                .andExpect(status().isBadRequest());
    }
}
