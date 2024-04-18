package com.redartis.dto.admin;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BugReportDto(
        Long id,
        String report,
        Long userId,
        LocalDateTime localDateTime,
        String username) {

}
