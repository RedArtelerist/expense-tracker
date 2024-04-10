package com.redartis.dto.analytics;

import lombok.Builder;

@Builder
public record AnalyticsDataMonthDto(
        String month,
        float totalIncome,
        float totalExpense) {
}
