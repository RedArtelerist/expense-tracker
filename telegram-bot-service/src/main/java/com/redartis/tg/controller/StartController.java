package com.redartis.tg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController {
    @GetMapping
    public String start() {
        return "Start from telegram bot service";
    }
}
