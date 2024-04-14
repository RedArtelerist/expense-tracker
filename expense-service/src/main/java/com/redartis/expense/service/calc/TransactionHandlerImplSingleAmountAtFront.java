package com.redartis.expense.service.calc;

import java.math.BigDecimal;

public class TransactionHandlerImplSingleAmountAtFront implements TransactionHandler {
    private static final String SPACE = " ";
    private static final char UA_DECIMAL_DELIMITER = ',';
    private static final char EN_DECIMAL_DELIMITER = '.';

    @Override
    public BigDecimal calculateAmount(String transaction) {
        String amountAsString = transaction.substring(0, transaction.indexOf(SPACE));
        return new BigDecimal(amountAsString.replace(UA_DECIMAL_DELIMITER, EN_DECIMAL_DELIMITER));
    }

    @Override
    public String getTransactionComment(String transaction) {
        return transaction.substring(transaction.indexOf(SPACE) + 1);
    }

    @Override
    public String getRegExp() {
        return "^(\\d*(\\,|\\.)?\\d+)(\\s+)([a-zA-Zа-яА-яії0-9\\p{P}\\s]+)";
    }
}
