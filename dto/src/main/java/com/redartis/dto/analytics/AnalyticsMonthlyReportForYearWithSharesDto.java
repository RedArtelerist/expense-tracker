package com.redartis.dto.analytics;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Builder;

@Builder
public record AnalyticsMonthlyReportForYearWithSharesDto(
        String categoryName,
        Map<Integer, BigDecimal> monthlyAnalytics,
        Map<Integer, Double> shareOfMonthlyExpenses) {
}
