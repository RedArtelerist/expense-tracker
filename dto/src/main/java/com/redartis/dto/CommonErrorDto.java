package com.redartis.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record CommonErrorDto(
        String code,
        String message,
        Instant timestamp) {
}
