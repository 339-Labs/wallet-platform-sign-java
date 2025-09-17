package com.labs339.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        System.out.println("ProviderApplication run");
        SpringApplication.run(ProviderApplication.class, args);
    }
}