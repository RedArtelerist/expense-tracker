package com.redartis.apigateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginPageController {
    @Value("${telegram.bot.name}")
    private String botName;

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/bot-login")
    public String getBotLogin() {
        return botName;
    }
}
