package com.playtika.automation.feign.carsshop.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.playtika.automation.feign.carsshop.model.Car;
import com.playtika.automation.feign.carsshop.model.CarId;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarsShopFeignTest {

    @Autowired
    private CarsShopFeign client;

    @Rule
    public WireMockRule wm = new WireMockRule(options().port(8082));

    @Test
    public void shouldReturnIdOfAddedCar() throws Exception {
        Car car = new Car("AEW123", "BMW", 2016, "red");

        stubFor(post(urlEqualTo("/cars/?price=2000&contacts=097876545"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\" = 1}")));

        CarId id = client.addCar(car, 2000, "097876545");
        assertThat(id.getId(), equalTo(1L));
    }
}
