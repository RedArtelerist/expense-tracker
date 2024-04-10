package com.redartis.tg.mapper;

import com.redartis.tg.model.MergeRequest;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Slf4j
public class MergeRequestMapper {
    private static final Boolean DEFAULT_COMPLETION = false;
    private static final Integer MILLISECONDS_CONVERSION = 1000;
    private static final ZoneOffset UA_OFFSET = ZoneOffset.of("+02:00");

    public MergeRequest mapMergeRequestMessageToMergeRequest(Message message) {
        var date = Instant.ofEpochMilli((long) message.getDate() * MILLISECONDS_CONVERSION)
                .atOffset(UA_OFFSET).toLocalDateTime();

        return MergeRequest.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .completed(DEFAULT_COMPLETION)
                .date(date)
                .build();
    }
}
