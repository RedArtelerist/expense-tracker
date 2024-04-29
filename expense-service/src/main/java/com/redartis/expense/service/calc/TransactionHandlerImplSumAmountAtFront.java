package com.redartis.expense.service.calc;

import com.redartis.expense.exception.TransactionProcessingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionHandlerImplSumAmountAtFront implements TransactionHandler {
    private final String regexpForAmountAtFront =
            "^(\\d*([,.])?\\d+)((\\s*)(\\+((\\s*)\\d*([,.])?\\d+)(\\s*)))+\\s+";

    @Override
    public BigDecimal calculateAmount(String transaction) {
        Pattern pattern = Pattern.compile(regexpForAmountAtFront);
        Matcher matcher = pattern.matcher(transaction);
        if (matcher.find()) {
            String amountAsString = matcher.group().trim()
                    .replace(UA_DECIMAL_DELIMITER, EN_DECIMAL_DELIMITER)
                    .replace(SPACE, NOTHING);
            return Arrays.stream(amountAsString.split(PLUS))
                    .map(amount -> new BigDecimal(amount.trim()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        throw new TransactionProcessingException("Unsupported transaction format");
    }

    @Override
    public String getTransactionComment(String transaction) {
        Pattern pattern = Pattern.compile(regexpForAmountAtFront);
        Matcher matcher = pattern.matcher(transaction);
        return matcher.replaceAll(NOTHING).trim();
    }

    @Override
    public String getRegExp() {
        return "^(\\d*(\\,|\\.)?\\d+)((\\s*)(\\+((\\s*)\\d*(\\,|\\.)?\\d+)"
               + "(\\s*)))+\\s+([a-zA-Zа-яА-яії0-9\\p{P}\\s]+)";
    }
}
