package com.redartis.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginPageController {
    @Value("${telegram.bot.name}")
    private String botName;

    @GetMapping("/bot-login")
    public String getBotLogin() {
        log.info("Bot name: {}", botName);

        return botName;
    }
}
