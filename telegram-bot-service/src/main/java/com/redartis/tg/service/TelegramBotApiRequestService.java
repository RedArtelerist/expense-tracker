package com.redartis.tg.service;

import com.redartis.tg.feign.TelegramBotApiFeign;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramBotApiRequestService {
    private final TelegramBotApiFeign telegramBotApiFeign;

    public byte[] getVoiceMessageBytes(String fileId) {
        return telegramBotApiFeign.getVoiceMessage(getTelegramFileUrl(fileId)).getBody();
    }

    private String getTelegramFileUrl(String fileId) {
        return Objects.requireNonNull(telegramBotApiFeign.getTelegramFileData(fileId).getBody())
                .getResult().getFilePath();
    }
}
