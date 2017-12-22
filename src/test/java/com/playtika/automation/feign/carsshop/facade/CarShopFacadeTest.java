package com.playtika.automation.feign.carsshop.facade;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.CarSaleDetails;
import com.playtika.automation.feign.carsshop.model.SaleInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by emuravjova on 12/22/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CarShopFacadeTest {

    @Autowired
    private CarShopFacade facade;

//    @Autowired
//    private CarsShopFeign client;

    @Rule
    public WireMockRule wm = new WireMockRule(options().port(8082).notifier(new Slf4jNotifier(true)));

    @Test
    public void shouldReturnReportForAddedCar() throws Exception {
        Car car = new Car("AEW123", "BMW", 2016, "red");

        wm.stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo("2000"))
                .withQueryParam("contacts", WireMock.equalTo("097876545"))
                .withRequestBody(equalToJson("{\"number\":\"AEW123\",\"brand\":\"BMW\",\"year\":2016,\"color\":\"red\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"id\":1}")));

        CarReport expectedReport = new CarReport(car, "Car(number=AEW123, brand=BMW, year=2016, color=red)has been added for sale with id=1");
        CarReport actualReport = facade.getCarReport(new CarSaleDetails(car, new SaleInfo(2000, "097876545")));
        assertThat(actualReport, equalTo(expectedReport));
    }

    @Test
    public void shouldReturnReportForCarThatAlreadyOnSale() throws Exception {
        Car car = new Car("AEW123", "BMW", 2016, "red");

        wm.stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo("2000"))
                .withQueryParam("contacts", WireMock.equalTo("097876545"))
                .withRequestBody(equalToJson("{\"number\":\"AEW123\",\"brand\":\"BMW\",\"year\":2016,\"color\":\"red\"}"))
                .willReturn(aResponse()
                        .withStatus(500)));

        CarReport expectedReport = new CarReport(car, "Car has not been added due to such car is already on sale");
        CarReport actualReport = facade.getCarReport(new CarSaleDetails(car, new SaleInfo(2000, "097876545")));
        assertThat(actualReport, equalTo(expectedReport));
    }

    @Test
    public void shouldReturnReportForCarWithMissingDetails() throws Exception {
        Car car = new Car("AEW123", null, 2016, "red");

        wm.stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo("2000"))
                .withRequestBody(equalToJson("{\"number\":\"AEW123\",\"brand\":null,\"year\":2016,\"color\":\"red\"}"))
                .willReturn(aResponse()
                        .withStatus(400)));

        CarReport expectedReport = new CarReport(car, "Car has not been added due to not all required parameters was received");
        CarReport actualReport = facade.getCarReport(new CarSaleDetails(car, new SaleInfo(2000, null)));
        assertThat(actualReport, equalTo(expectedReport));
    }
}
