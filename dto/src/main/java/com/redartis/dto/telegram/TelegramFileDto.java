package com.redartis.dto.telegram;

public record TelegramFileDto(byte[] voiceMessage, String fileUrl) {
}
