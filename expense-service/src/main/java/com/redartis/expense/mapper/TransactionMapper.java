package com.redartis.expense.mapper;

import com.redartis.dto.constants.Type;
import com.redartis.dto.transaction.TransactionDto;
import com.redartis.dto.transaction.TransactionResponseDto;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.util.NumericalUtils;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    private static final String INCOME = "Incomes";
    private static final String EXPENSE = "Expenses";
    private static final String CATEGORY_UNDEFINED = "Undefined";

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

    public TransactionResponseDto mapTransactionToTelegramResponse(Transaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .type(getTransactionType(transaction))
                .category(getTransactionCategory(transaction))
                .amount(NumericalUtils.roundAmount(transaction.getAmount()).toString())
                .chatId(transaction.getAccount().getChatId())
                .comment(transaction.getMessage())
                .build();
    }

    private String getTransactionType(Transaction transaction) {
        if (Objects.isNull(transaction.getCategory())
                || transaction.getCategory().getType() == Type.EXPENSE) {
            return EXPENSE;
        }
        return INCOME;
    }

    private String getTransactionCategory(Transaction transaction) {
        if (Objects.isNull(transaction.getCategory())) {
            return CATEGORY_UNDEFINED;
        }
        return transaction.getCategory().getName();
    }
}
