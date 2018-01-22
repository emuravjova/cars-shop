package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.model.Customer;
import com.playtika.automation.feign.carsshop.model.DealInfo;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarShopControllerSystemTest {

    @Test
    public void shouldTestAllCarSaleFlow() throws Exception {
        //add car on sale
        ArrayList carIdList = given()
                .body("src/test/resources/files/systemTest.csv")
                .when()
                .log().all()
                .post("/cars")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0].carId", Matchers.greaterThan(0))
                .body("[0].carDetails.car.number", equalTo("js123"))
                .body("[0].carDetails.car.brand", equalTo("BMW"))
                .body("[0].carDetails.car.year", equalTo(2015))
                .body("[0].carDetails.car.color", equalTo("green"))
                .body("[0].carDetails.saleInfo.price", equalTo(15000))
                .body("[0].carDetails.saleInfo.contacts", equalTo("09689638521"))
                .body("[0].status", equalTo("ADDED")).extract().response().jsonPath().get("carId");
        Integer carId = ((Integer) carIdList.get(0));

        //create 2 deals for added car
        Customer customer = new Customer("Den", "0896543456");
        DealInfo firstDeal = createDealAndReturn(Long.valueOf(carId), customer, 3000);
        DealInfo secondDeal = createDealAndReturn(Long.valueOf(carId), customer, 2000);
        assertThat(firstDeal.getOfferId(), equalTo(secondDeal.getOfferId()));

        //get the best deal
        given()
                .when().get("/offer/{id}", firstDeal.getOfferId())
                .then()
                .log().all()
                .body("id", equalTo(((int) firstDeal.getId())))
                .body("offerId", equalTo((int) secondDeal.getOfferId()))
                .statusCode(200);

        //accept and reject deals
        given()
                .when().put("/acceptDeal/{firstDeal}", firstDeal.getId())
                .then()
                .log().all()
                .statusCode(200);
        given()
                .when().put("/rejectDeal/{secondDeal}", secondDeal.getId())
                .then()
                .log().all()
                .statusCode(200);

        //no more deal could not be created for car
        given()
                .contentType("application/json")
                .body(customer)
                .when().post("/deal?price=25000&carId={id}", carId)
                .then()
                .log().all()
                .statusCode(404);
    }

    private DealInfo createDealAndReturn(Long id, Customer customer, int price) {
        return given()
                .contentType("application/json")
                .queryParam("price", price)
                .queryParam("carId", id)
                .body(customer)
                .when()
                .log().all()
                .post("/deal")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", greaterThan(0))
                .extract().as(DealInfo.class);
    }
}
