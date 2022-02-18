package com.semibit.stocksmate;

import com.semibit.stocksmate.automate.Ticker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StocksmateApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocksmateApplication.class, args);
        Ticker ticker = new Ticker();
        ticker.start();
    }

}
