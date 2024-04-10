package com.redartis.tg.mapper;

import com.redartis.dto.transaction.TransactionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public String mapTransactionResponseToTelegramMessage(
            TransactionResponseDto transactionResponseDto) {
        return "Saved to " + transactionResponseDto.type()
                + " -> " + transactionResponseDto.category()
                + ". Amount: " + transactionResponseDto.amount()
                + " Description: " + transactionResponseDto.comment();
    }
}
