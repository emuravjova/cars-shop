package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.ReportStatus;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarShopControllerSystemTest {
    private final static String NUMBER = "FG123";
    private final static String BRAND = "BMW";
    private final static Integer YEAR = 2015;
    private final static String COLOR = "green";
    private final static Integer PRICE = 15000;
    private final static String CONTACTS = "09689638521";

    @Test
    public void shouldGetCars() throws Exception {
        given()
                .body("src/test/resources/files/systemTest.csv")
                .when()
                .log().all()
                .post("/cars")
                .then()
                .log().all()
                .body("[0].carDetails.car.number", equalTo(NUMBER))
                .body("[0].carDetails.car.brand", equalTo(BRAND))
                .body("[0].carDetails.car.year", equalTo(YEAR))
                .body("[0].carDetails.car.color", equalTo(COLOR))
                .body("[0].carDetails.saleInfo.price", equalTo(PRICE))
                .body("[0].carDetails.saleInfo.contacts", equalTo(CONTACTS))
                .body("[0].status", equalTo("ADDED"));
    }
}
