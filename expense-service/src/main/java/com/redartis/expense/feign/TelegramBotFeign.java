package com.redartis.expense.feign;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "telegram-bot-service")
public interface TelegramBotFeign {
    @PostMapping("/merge")
    void sendMergeRequest(@RequestParam Long userId);

    @DeleteMapping("/telegram-message/{id}")
    void deleteTelegramMessageById(@PathVariable("id") UUID id);
}
