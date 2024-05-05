package com.redartis.tg.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "merge_requests")
@Data
@Builder
public class MergeRequest {
    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "chat_id")
    private Long chatId;

    @Field(name = "message_id")
    private Integer messageId;

    @Field(name = "completed")
    private Boolean completed;

    @Field(name = "date")
    private LocalDateTime date;
}
