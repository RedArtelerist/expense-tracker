package com.redartis.tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration;

@EnableFeignClients
@SpringBootApplication
@Import(TelegramBotStarterConfiguration.class)
public class TelegramBotServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramBotServiceApplication.class, args);
    }
}
