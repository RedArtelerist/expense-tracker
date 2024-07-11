package com.redartis.apigateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginPageController {
    @Value("${telegram.bot.name}")
    private String botName;

    @GetMapping("/bot-login")
    public String getBotLogin() {
        return botName;
    }
}
