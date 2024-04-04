package com.redartis.dto.transaction;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class TransactionMessageDto {
    private String message;
    private Long userId;
    private Long chatId;
    private LocalDateTime date;
}
