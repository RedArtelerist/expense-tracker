package com.redartis.tg.mapper;

import com.redartis.dto.transaction.TransactionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public String mapTransactionResponseToTelegramMessage(
            TransactionResponseDto transactionResponseDto) {
        return """
                Saved to %s -> %s.
                Amount: %s.
                Description: %s.""".formatted(
                transactionResponseDto.type(),
                transactionResponseDto.category(),
                transactionResponseDto.amount(),
                transactionResponseDto.comment()
        );
    }
}
