package com.labs339.platform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AddressApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.out.println("AddressApplication run");
        SpringApplication.run(AddressApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("AddressApplication started successfully!");
    }

}