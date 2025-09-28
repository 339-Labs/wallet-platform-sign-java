package com.labs339.platform;

import com.labs339.platform.dao.mapper.ChainConfigMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan("com.labs339.platform.dao.mapper")
public class BusinessApplication implements CommandLineRunner {
    public static void main(String[] args) {
        System.out.println("BusinessApplication run");
        SpringApplication.run(BusinessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("BusinessApplication started successfully!");
    }
}