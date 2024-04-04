package com.redartis.dto.transaction;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TransactionResponseDto(
        UUID id,
        String type,
        String category,
        String amount,
        String comment,
        Long chatId) {
}
