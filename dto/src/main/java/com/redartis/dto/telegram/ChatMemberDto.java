package com.redartis.dto.telegram;

import lombok.Builder;

@Builder
public record ChatMemberDto(
        Long chatId,
        Long userId,
        String username,
        String firstName,
        String lastName) {
}
