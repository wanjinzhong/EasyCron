package com.neil.easycron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages = "com.neil.easycron.dao.entity")
@EnableJpaRepositories(basePackages = "com.neil.easycron.dao.repository")
@EnableAsync
public class EasycronApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasycronApplication.class, args);
    }

}

