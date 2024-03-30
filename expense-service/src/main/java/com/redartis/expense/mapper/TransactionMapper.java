package com.redartis.expense.mapper;

import com.redartis.dto.transaction.TransactionDto;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.util.NumericalUtils;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDto mapTransactionToDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(NumericalUtils.roundAmount(transaction.getAmount()))
                .message(transaction.getMessage())
                .date(transaction.getDate())
                .suggestedCategoryId(transaction.getSuggestedCategoryId())
                .telegramUserId(transaction.getTelegramUserId())
                .categoryName(transaction.getCategory() != null
                        ? transaction.getCategory().getName()
                        : null
                )
                .build();
    }
}
