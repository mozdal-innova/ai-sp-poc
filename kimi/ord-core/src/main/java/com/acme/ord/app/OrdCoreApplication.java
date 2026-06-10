package com.acme.ord.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.acme.ord")
public class OrdCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdCoreApplication.class, args);
    }

}
