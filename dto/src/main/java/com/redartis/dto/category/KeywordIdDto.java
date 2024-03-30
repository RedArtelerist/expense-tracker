package com.redartis.dto.category;

import lombok.Builder;

@Builder
public record KeywordIdDto(
        Long accountId,
        String name) {
}
