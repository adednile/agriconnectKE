package com.ecommerce.agriconnectke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgriconnectkeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriconnectkeApplication.class, args);
        System.out.println("AgriConnectKE Application started on port 8082");
    }
}