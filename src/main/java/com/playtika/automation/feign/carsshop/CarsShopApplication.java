package com.playtika.automation.feign.carsshop;

import com.playtika.automation.feign.carsshop.client.CarsShopFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class CarsShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsShopApplication.class, args);
    }
}
