package com.playtika.automation.feign.carsshop.facade;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.playtika.automation.feign.carsshop.model.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

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

    @Rule
    public WireMockRule wm = new WireMockRule(options().port(8082).notifier(new Slf4jNotifier(true)));

    private final static String NUMBER = "AS123";
    private final static String BRAND = "BMW";
    private final static Integer YEAR = 2007;
    private final static String COLOR = "blue";
    private final static Integer PRICE = 25000;
    private final static String CONTACTS = "0982345678";
    private final ObjectMapper carMapper = new ObjectMapper();

    @Test
    public void shouldReturnReportForAddedCar() throws Exception {
        Car car = new Car(NUMBER, BRAND, YEAR, COLOR);
        CarSaleDetails carWithDetails = new CarSaleDetails(car, new SaleInfo(PRICE,CONTACTS));
        wm.stubFor(doRequestToAddCar(car)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"id\":1}")));

        CarReport expectedReport = new CarReport(carWithDetails, ReportStatus.ADDED);
        CarReport actualReport = facade.getCarReport(carWithDetails);
        assertThat(actualReport, equalTo(expectedReport));
    }

    @Test
    public void shouldReturnReportForCarThatAlreadyOnSale() throws Exception {
        Car car = new Car(NUMBER, BRAND, YEAR, COLOR);
        CarSaleDetails carWithDetails = new CarSaleDetails(car, new SaleInfo(PRICE,CONTACTS));
        wm.stubFor(doRequestToAddCar(car)
                .willReturn(aResponse()
                        .withStatus(500)));

        CarReport expectedReport = new CarReport(carWithDetails, ReportStatus.ALREADY_ON_SALE);
        CarReport actualReport = facade.getCarReport(carWithDetails);
        assertThat(actualReport, equalTo(expectedReport));
    }

    @Test
    public void shouldReturnReportForCarWithMissingDetails() throws Exception {
        Car car = new Car(NUMBER, null, YEAR, COLOR);
        CarSaleDetails carWithDetails = new CarSaleDetails(car, new SaleInfo(PRICE,null));
        wm.stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo(String.valueOf(PRICE)))
                .withRequestBody(equalToJson(carMapper.writeValueAsString(car)))
                .willReturn(aResponse()
                        .withStatus(400)));

        CarReport expectedReport = new CarReport(carWithDetails, ReportStatus.NOT_ALL_PARAMETERS);
        CarReport actualReport = facade.getCarReport(carWithDetails);
        assertThat(actualReport, equalTo(expectedReport));
    }

    private MappingBuilder doRequestToAddCar(Car car) throws JsonProcessingException {
        return post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo(String.valueOf(PRICE)))
                .withQueryParam("contacts", WireMock.equalTo(CONTACTS))
                .withRequestBody(equalToJson(carMapper.writeValueAsString(car)));
    }
}
