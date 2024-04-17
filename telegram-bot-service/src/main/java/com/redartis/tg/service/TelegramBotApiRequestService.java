package com.redartis.tg.service;

import com.redartis.dto.telegram.TelegramFileDto;
import com.redartis.tg.feign.TelegramBotApiFeign;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotApiRequestService {
    private static final String FILE_URL_PLACEHOLDER = "%s/file/bot%s/%s";
    private final TelegramBotApiFeign telegramBotApiFeign;

    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.api.url}")
    private String botApiHost;

    public TelegramFileDto getFileData(String fileId) {
        String telegramFileUrl = getTelegramFileUrl(fileId);
        String fileUrl = String.format(FILE_URL_PLACEHOLDER, botApiHost, botToken, telegramFileUrl);
        byte[] voiceMessageBytes = telegramBotApiFeign.getVoiceMessage(telegramFileUrl).getBody();
        return new TelegramFileDto(voiceMessageBytes, fileUrl);
    }

    private String getTelegramFileUrl(String fileId) {
        ApiResponse<File> response = telegramBotApiFeign.getTelegramFileData(fileId).getBody();
        return Objects.requireNonNull(response)
                .getResult()
                .getFilePath();
    }
}
