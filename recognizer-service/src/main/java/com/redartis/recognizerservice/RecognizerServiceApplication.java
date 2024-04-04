package com.redartis.recognizerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RecognizerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecognizerServiceApplication.class, args);
    }
}
