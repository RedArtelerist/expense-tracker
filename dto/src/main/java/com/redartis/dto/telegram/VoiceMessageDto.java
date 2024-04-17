package com.redartis.dto.telegram;

import lombok.Builder;

@Builder
public record VoiceMessageDto(
        byte[] voiceMessageBytes,
        String fileUrl,
        Long userId,
        Long chatId) {
}
