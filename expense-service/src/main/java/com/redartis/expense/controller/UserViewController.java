package com.redartis.expense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/history")
    public String getTransactionPage() {
        return "history";
    }
}
