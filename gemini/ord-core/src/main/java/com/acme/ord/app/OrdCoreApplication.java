package com.acme.ord.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.acme.ord")
@EntityScan(basePackages = "com.acme.ord.domain.entity")
@EnableJpaRepositories(basePackages = "com.acme.ord.domain.repository")
public class OrdCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdCoreApplication.class, args);
    }
}
