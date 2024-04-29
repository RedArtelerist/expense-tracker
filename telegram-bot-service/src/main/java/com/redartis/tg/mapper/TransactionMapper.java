package com.redartis.tg.mapper;

import com.redartis.dto.transaction.TransactionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public String mapTransactionResponseToTelegramMessage(
            TransactionResponseDto transactionResponseDto) {
        return """
                Збережено до %s -> %s.
                Ціна: %s.
                Опис: %s.""".formatted(
                transactionResponseDto.type(),
                transactionResponseDto.category(),
                transactionResponseDto.amount(),
                transactionResponseDto.comment()
        );
    }
}
