package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.model.Car;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarShopControllerSystemTest {
    private final static String NUMBER = "ATT123";
    private final static String BRAND = "BMW";
    private final static Integer YEAR = 2007;
    private final static String COLOR = "blue";
    private final static String MESSAGE = "Car(number=AEW123, brand=BMW, year=2016, color=red)has been added for sale with id=1";

    @Test
    public void shouldGetCars() throws Exception {
        JsonPath jsonResponse = given()
                .body("src/main/resources/cars/CarsToAddTest.csv")
                .when().post("/cars").jsonPath();
        assert (jsonResponse.get("car.number").equals(NUMBER));
        assert (jsonResponse.get("car.brand").equals(BRAND));
        assert (jsonResponse.get("car.year").equals(YEAR));
        assert (jsonResponse.get("car.color").equals(COLOR));
        assert (jsonResponse.get("message").equals(MESSAGE));
    }
}
