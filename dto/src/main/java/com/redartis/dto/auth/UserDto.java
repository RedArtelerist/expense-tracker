package com.redartis.dto.auth;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        String firstName) {
}
