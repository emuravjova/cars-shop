package com.playtika.automation.feign.carsshop.facade;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.playtika.automation.feign.carsshop.exception.CarOnSaleNotFoundException;
import com.playtika.automation.feign.carsshop.exception.NoBestDealFoundException;
import com.playtika.automation.feign.carsshop.exception.NotAllRequiredParametersReceivedException;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldReturnReportForAddedCar() throws Exception {
        Car car = new Car(NUMBER, BRAND, YEAR, COLOR);
        CarSaleDetails carWithDetails = new CarSaleDetails(car, new SaleInfo(PRICE,CONTACTS));
        wm.stubFor(doRequestToAddCar(car)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"id\":1}")));

        CarReport expectedReport = new CarReport(1L, carWithDetails, ReportStatus.ADDED);
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

        CarReport expectedReport = new CarReport(0L, carWithDetails, ReportStatus.ALREADY_ON_SALE);
        CarReport actualReport = facade.getCarReport(carWithDetails);
        assertThat(actualReport, equalTo(expectedReport));
    }

    @Test
    public void shouldReturnReportForCarWithMissingDetails() throws Exception {
        Car car = new Car(NUMBER, null, YEAR, COLOR);
        CarSaleDetails carWithDetails = new CarSaleDetails(car, new SaleInfo(PRICE,null));
        wm.stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo(String.valueOf(PRICE)))
                .withRequestBody(equalToJson(createCarInJson(car.getNumber(),car.getBrand(),car.getYear(),car.getColor())))
                .willReturn(aResponse()
                        .withStatus(400)));

        CarReport expectedReport = new CarReport(0L, carWithDetails, ReportStatus.NOT_ALL_PARAMETERS);
        CarReport actualReport = facade.getCarReport(carWithDetails);
        assertThat(actualReport, equalTo(expectedReport));
    }

    @Test
    public void shouldReturnDealInfoOnDealCreation() throws Exception {
        DealInfo dealInfo = new DealInfo(1L, 1L);
        Customer customer = new Customer("Den","1111");
        wm.stubFor(doRequestToAddDeal(customer)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"id\":1, \"offerId\":1}")));
        assertThat(facade.createDeal(customer, 25000, 1L), equalTo(dealInfo));
    }

    @Test(expected = CarOnSaleNotFoundException.class)
    public void shouldThrowExceptionWhenCarNotOnSAleOnDealCreation() throws Exception {
        Customer customer = new Customer("Den","1111");
        wm.stubFor(doRequestToAddDeal(customer)
                .willReturn(aResponse()
                                .withStatus(404)));
        facade.createDeal(customer, 25000, 1L);
    }

    @Test(expected = NotAllRequiredParametersReceivedException.class)
    public void shouldThrowExceptionWhenNotAllParamsOnDealCreation() throws Exception {
        Customer customer = new Customer("Den","1111");
        wm.stubFor(doRequestToAddDeal(customer)
                .willReturn(aResponse()
                        .withStatus(400)));
        facade.createDeal(customer, 25000, 1L);
    }

    @Test
    public void shouldReturnDealInfoWhenBestDealFound() throws Exception {
        DealInfo dealInfo = new DealInfo(1L, 1L);
        wm.stubFor(get(urlPathEqualTo("/offer/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"id\":1, \"offerId\":1}")));
        assertThat(facade.findTheBestDeal(1L), equalTo(dealInfo));
    }

    @Test(expected = NoBestDealFoundException.class)
    public void shouldThrowExceptionWhenNoBestDealFound(){
        wm.stubFor(get(urlPathEqualTo("/offer/1"))
                .willReturn(aResponse()
                                .withStatus(404)));
        facade.findTheBestDeal(1L);
    }

    @Test
    public void shouldReturnTrueWhenDealAccepted() throws Exception {
        wm.stubFor(put(urlPathEqualTo("/acceptDeal/1"))
                .willReturn(aResponse()
                        .withStatus(200)));
        assertTrue(facade.acceptDeal(1L));
    }

    @Test
    public void shouldReturnFalseWhenDealIsNotAccepted() throws Exception {
        wm.stubFor(put(urlPathEqualTo("/acceptDeal/1"))
                .willReturn(aResponse()
                        .withStatus(409)));
        assertFalse(facade.acceptDeal(1L));
    }

    @Test
    public void shouldDoNotThrowAnyExceptionWhenDealRejected(){
        wm.stubFor(put(urlPathEqualTo("/rejectDeal/1"))
                .willReturn(aResponse()
                        .withStatus(200)));
        facade.rejectDeal(1L);
    }

    private MappingBuilder doRequestToAddCar(Car car) throws JsonProcessingException {
        return post(urlPathEqualTo("/cars"))
                .withQueryParam("price", WireMock.equalTo(String.valueOf(PRICE)))
                .withQueryParam("contacts", WireMock.equalTo(CONTACTS))
                .withRequestBody(equalToJson(createCarInJson(car.getNumber(),car.getBrand(),car.getYear(),car.getColor())));
    }

    private MappingBuilder doRequestToAddDeal(Customer customer) throws JsonProcessingException {
        return post(urlPathEqualTo("/deal"))
                .withQueryParam("price", WireMock.equalTo(String.valueOf(PRICE)))
                .withQueryParam("carId", WireMock.equalTo(String.valueOf(1L)))
                .withRequestBody(equalToJson(createCustomerInJson(customer.getName(),customer.getContacts())));
    }

    private static String createCustomerInJson(String name, String contacts) {
        return String.format("{\"name\": \"%s\", \"contacts\":\"%s\"}", name, contacts);
    }

    private static String createCarInJson(String number, String brand, Integer year, String color) {
        return String.format("{\"number\": \"%s\", \"brand\":\"%s\", \"year\": %s, \"color\":\"%s\"}", number, brand, year, color);

    }
}
