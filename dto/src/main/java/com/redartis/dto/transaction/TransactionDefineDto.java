package com.redartis.dto.transaction;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TransactionDefineDto(
        UUID transactionId,
        Long categoryId) {
}
