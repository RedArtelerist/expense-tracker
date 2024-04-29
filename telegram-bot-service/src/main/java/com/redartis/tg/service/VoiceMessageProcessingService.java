package com.redartis.tg.service;

import static com.redartis.tg.util.TelegramBotAnswer.VOICE_MESSAGE_TOO_LONG;

import com.redartis.dto.telegram.TelegramFileDto;
import com.redartis.dto.telegram.VoiceMessageDto;
import com.redartis.tg.exception.InvalidVoiceDurationException;
import com.redartis.tg.feign.RecognizerFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Voice;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceMessageProcessingService {
    @Value("${bot.voice.max_length}")
    private int voiceMessageMaxLength;
    private final TelegramBotApiRequestService telegramBotApiRequestService;
    private final RecognizerFeign recognizerFeign;

    public String processVoiceMessage(Voice voiceMessage, Long userId, Long chatId) {
        TelegramFileDto telegramFileDto = telegramBotApiRequestService.getFileData(
                voiceMessage.getFileId()
        );
        log.info(telegramFileDto.fileUrl());

        if (voiceMessage.getDuration() > voiceMessageMaxLength) {
            throw new InvalidVoiceDurationException(
                    String.format(VOICE_MESSAGE_TOO_LONG, voiceMessageMaxLength)
            );
        }

        return recognizerFeign.recognizeVoiceMessage(VoiceMessageDto.builder()
                .voiceMessageBytes(telegramFileDto.voiceMessage())
                .fileUrl(telegramFileDto.fileUrl())
                .userId(userId)
                .chatId(chatId)
                .build());
    }
}
