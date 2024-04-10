package com.redartis.tg.service;

import com.redartis.tg.model.TelegramMessage;
import com.redartis.tg.repository.TelegramMessageRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramMessageService {
    private final TelegramMessageRepository telegramMessageRepository;
    private final ExpenseRequestService expenseRequestService;

    public void saveTelegramMessage(TelegramMessage telegramMessage) {
        telegramMessageRepository.save(telegramMessage);
    }

    @Transactional
    public void deleteTelegramMessageByTransactionId(UUID id) {
        telegramMessageRepository.deleteByTransactionId(id);
    }

    public void deleteTransactionById(Integer messageId, Long chatId) {
        var telegramMessage = telegramMessageRepository.findByMessageIdAndChatId(messageId, chatId);
        expenseRequestService.deleteTransactionById(telegramMessage.getTransactionId());
    }

    public TelegramMessage getTelegramMessageByMessageIdAndChatId(Integer messageId, Long chatId) {
        return telegramMessageRepository.findByMessageIdAndChatId(messageId, chatId);
    }
}
