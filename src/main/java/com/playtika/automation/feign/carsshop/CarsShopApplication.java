package com.playtika.automation.feign.carsshop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class CarsShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsShopApplication.class, args);
    }
}
