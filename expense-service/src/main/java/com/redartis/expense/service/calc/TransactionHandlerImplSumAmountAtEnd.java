package com.redartis.expense.service.calc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TransactionHandlerImplSumAmountAtEnd implements TransactionHandler {
    private final String regexpForAmountAtEnd =
            "(\\s+)((\\s*)(\\d*([,.])?\\d+)(\\s*)\\+(\\s*))*((\\s*)\\d*([,.])?\\d+)$";

    @Override
    public BigDecimal calculateAmount(String transaction) {
        Pattern pattern = Pattern.compile(regexpForAmountAtEnd);
        Matcher matcher = pattern.matcher(transaction);
        matcher.find();
        String amountAsString = matcher.group().trim()
                .replace(UA_DECIMAL_DELIMITER, EN_DECIMAL_DELIMITER)
                .replace(SPACE, NOTHING);
        Stream<String> amountAsStream = Arrays.stream(amountAsString.split(PLUS));
        final float[] sum = {0};
        amountAsStream.forEach(t -> sum[0] += Float.parseFloat(t));
        return BigDecimal.valueOf(sum[0]);
    }

    @Override
    public String getTransactionComment(String transaction) {
        Pattern pattern = Pattern.compile(regexpForAmountAtEnd);
        Matcher matcher = pattern.matcher(transaction);
        return matcher.replaceAll(NOTHING).trim();
    }

    @Override
    public String getRegExp() {
        return "([a-zA-Zа-яА-яії0-9\\p{P}\\s]+)(\\s+)((\\s*)(\\d*(\\,|\\.)?\\d+)"
               + "(\\s*)\\+(\\s*))*((\\s*)\\d*(\\,|\\.)?\\d+)$";
    }
}
