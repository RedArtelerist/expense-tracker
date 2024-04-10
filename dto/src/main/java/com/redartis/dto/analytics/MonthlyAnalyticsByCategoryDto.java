package com.redartis.dto.analytics;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record MonthlyAnalyticsByCategoryDto(
        BigDecimal amount,
        String categoryName,
        Integer month) {
}
