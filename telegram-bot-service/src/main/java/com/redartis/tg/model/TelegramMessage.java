package com.redartis.tg.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "telegram_messages")
@Data
@Builder
public class TelegramMessage {
    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "message_id")
    private Integer messageId;

    @Field(name = "chat_id")
    private Long chatId;

    @Field(name = "transaction_id")
    private UUID transactionId;
}
