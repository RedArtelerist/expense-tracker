package com.redartis.dto.analytics;

import lombok.Builder;

@Builder
public record AnalyticsDataDto(
        Long categoryId,
        String categoryName,
        Number mediumAmountOfTransactions) {
}
