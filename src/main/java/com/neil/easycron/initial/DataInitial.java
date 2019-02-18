package com.neil.easycron.initial;
import com.neil.easycron.service.DataInitialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DataInitial implements CommandLineRunner {

    @Autowired
    private DataInitialService dataInitialService;
    @Override
    public void run(String... args) throws Exception {
        dataInitialService.init();
    }
}
