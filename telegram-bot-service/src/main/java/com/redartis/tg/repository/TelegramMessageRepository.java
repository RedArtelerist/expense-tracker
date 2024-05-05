package com.redartis.tg.repository;

import com.redartis.tg.model.TelegramMessage;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMessageRepository extends MongoRepository<TelegramMessage, Integer> {
    void deleteByTransactionId(UUID id);

    TelegramMessage findByMessageIdAndChatId(Integer messageId, Long chatId);

    void deleteByChatId(Long chatId);
}
