package com.redartis.dto.telegram;

public record ChatMemberDto(
        Long chatId,
        Long userId,
        String username,
        String firstName,
        String lastName) {
}
