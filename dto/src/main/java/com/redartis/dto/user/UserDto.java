package com.redartis.dto.user;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        String firstName) {
}
