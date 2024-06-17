package com.redartis.expense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/history")
    public String getTransactionPage() {
        return "history";
    }

    @GetMapping("/analytics")
    public String getAnalytics() {
        return "analytics";
    }

    @GetMapping("/micromanagement")
    public String getMicromanagement() {
        return "micromanagement";
    }

    @GetMapping("/settings")
    public String getSettings() {
        return "settings";
    }
}
