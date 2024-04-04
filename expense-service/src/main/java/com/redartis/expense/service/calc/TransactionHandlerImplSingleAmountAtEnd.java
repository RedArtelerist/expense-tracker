package com.redartis.expense.service.calc;

import java.math.BigDecimal;

public class TransactionHandlerImplSingleAmountAtEnd implements TransactionHandler {
    @Override
    public BigDecimal calculateAmount(String transaction) {
        String amountAsString = transaction.substring(transaction.lastIndexOf(SPACE) + 1);
        return new BigDecimal(amountAsString.replace(UA_DECIMAL_DELIMITER, EN_DECIMAL_DELIMITER));
    }

    @Override
    public String getTransactionComment(String transaction) {
        return transaction.substring(0, transaction.lastIndexOf(SPACE)).trim();
    }

    @Override
    public String getRegExp() {
        return "^([a-zA-Zа-яА-я0-9\\p{P}\\s]+)(\\s+)(\\d*(\\,|\\.)?\\d+)$";
    }
}
