package com.redartis.expense.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class NumericalUtils {
    private static final int SCALE = 2;

    public static Double roundAmount(Double amount) {
        return Optional.ofNullable(amount)
                .map(doubleAmount -> new BigDecimal(doubleAmount)
                        .setScale(SCALE, RoundingMode.HALF_UP)
                        .doubleValue()
                ).orElse(null);
    }

    public static Float roundAmount(Float amount) {
        return Optional.ofNullable(amount)
                .map(floatAmount -> new BigDecimal(floatAmount)
                        .setScale(SCALE, RoundingMode.HALF_UP)
                        .floatValue()
                ).orElse(null);
    }

    public static BigDecimal roundAmount(BigDecimal amount) {
        return Optional.ofNullable(amount)
                .map(bigDecimal -> bigDecimal.setScale(SCALE, RoundingMode.HALF_UP))
                .orElse(null);
    }
}
