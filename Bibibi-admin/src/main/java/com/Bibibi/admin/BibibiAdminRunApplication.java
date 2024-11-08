package com.Bibibi.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication(scanBasePackages = {"com.Bibibi"})
@MapperScan(basePackages = {"com.Bibibi.mappers"})
@SpringBootApplication(scanBasePackages = "com.Bibibi")
@EnableTransactionManagement
@EnableScheduling
public class BibibiAdminRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(BibibiAdminRunApplication.class, args);
    }
}
