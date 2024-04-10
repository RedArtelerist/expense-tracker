package com.redartis.dto.analytics;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Builder;

@Builder
public record AnalyticsMonthlyReportForYearDto(
        String categoryName,
        Map<Integer, BigDecimal> monthlyAnalytics) {
}
