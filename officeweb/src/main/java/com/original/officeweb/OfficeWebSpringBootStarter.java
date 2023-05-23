package com.original.officeweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.epoint"})
@ConfigurationPropertiesScan("com.epoint")
@EnableCaching
public class OfficeWebSpringBootStarter {

    public static void main(String[] args) {
        SpringApplication.run(OfficeWebSpringBootStarter.class, args);
    }
    
}
