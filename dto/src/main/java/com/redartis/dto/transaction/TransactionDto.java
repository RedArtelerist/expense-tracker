package com.redartis.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private UUID id;
    private String categoryName;
    private String message;
    private BigDecimal amount;
    private LocalDateTime date;
    private Long suggestedCategoryId;
    private Long telegramUserId;
    private String telegramUserName;
}
