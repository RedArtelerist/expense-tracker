package com.redartis.tg.controller;

import com.redartis.tg.service.TelegramMessageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram-message")
@RequiredArgsConstructor
public class TelegramMessageController {
    private final TelegramMessageService telegramMessageService;

    @DeleteMapping("/{id}")
    public void deleteTelegramMessageByIdTransaction(@PathVariable("id") UUID id) {
        telegramMessageService.deleteTelegramMessageByTransactionId(id);
    }
}
