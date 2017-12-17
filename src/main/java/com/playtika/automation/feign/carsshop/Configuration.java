package com.playtika.automation.feign.carsshop;

import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    CarsShopFeign CarsShopFeign(){
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(CarsShopFeign.class, "http://localhost:8082");
    }
}
