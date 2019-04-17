package com.neil.easycron.initial;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class EmailConfigInitial implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

    }
}
