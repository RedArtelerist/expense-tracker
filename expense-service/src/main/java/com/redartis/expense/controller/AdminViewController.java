package com.redartis.expense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @GetMapping("/panel")
    public String getPanel() {
        return "admin_panel";
    }

    @GetMapping("/stat")
    public String getStat() {
        return "admin_stat";
    }

    @GetMapping("/bugReports")
    public String getBugReports() {
        return "admin_bugreports";
    }
}
