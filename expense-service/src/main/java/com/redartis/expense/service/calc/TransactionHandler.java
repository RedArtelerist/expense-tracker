package com.redartis.expense.service.calc;

import java.math.BigDecimal;

public interface TransactionHandler {
    String SPACE = " ";
    String NOTHING = "";
    String PLUS = "\\+";
    char UA_DECIMAL_DELIMITER = ',';
    char EN_DECIMAL_DELIMITER = '.';

    BigDecimal calculateAmount(String transaction);

    String getTransactionComment(String transaction);

    String getRegExp();
}
