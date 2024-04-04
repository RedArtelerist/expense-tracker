package com.redartis.expense.service.calc;

import com.redartis.expense.exception.TransactionProcessingException;
import java.math.BigDecimal;

public class TransactionHandlerImplInvalidTransaction implements TransactionHandler {
    @Override
    public BigDecimal calculateAmount(String transaction) {
        throw new TransactionProcessingException("Unsupported transaction format");
    }

    @Override
    public String getTransactionComment(String transaction) {
        throw new TransactionProcessingException("Unsupported transaction format");
    }

    @Override
    public String getRegExp() {
        return ".*";
    }
}
