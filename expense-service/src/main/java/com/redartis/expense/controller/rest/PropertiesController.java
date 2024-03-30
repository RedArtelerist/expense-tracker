package com.redartis.expense.controller.rest;

import com.redartis.expense.config.properties.LongPollingTrackerProperties;
import com.redartis.expense.util.TelegramUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertiesController {
    private final LongPollingTrackerProperties longPollingTrackerProperties;
    private final TelegramUtils telegramUtils;

    @GetMapping("/longPolling")
    public Integer getLongPollingData() {
        return longPollingTrackerProperties.getPeriodOfInactivity();
    }

    @GetMapping("/telegramBotName")
    public String getTelegramBotName() {
        return telegramUtils.getTelegramBotName();
    }
}
