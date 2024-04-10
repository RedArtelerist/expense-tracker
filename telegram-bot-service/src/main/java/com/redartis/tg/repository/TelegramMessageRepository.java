package com.redartis.tg.repository;

import com.redartis.tg.model.TelegramMessage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMessageRepository extends JpaRepository<TelegramMessage, Integer> {
    void deleteByTransactionId(UUID id);

    TelegramMessage findByMessageIdAndChatId(Integer messageId, Long chatId);
}
