package com.redartis.recognizerservice.service;

import com.redartis.dto.telegram.VoiceMessageDto;
import com.redartis.recognizerservice.service.voice.VoiceRecognitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VoiceMessageService {
    private final WordsToNumbersService wordsToNumbersService;
    private final VoiceRecognitionService voiceRecognitionService;

    public VoiceMessageService(WordsToNumbersService wordsToNumbersService,
                               @Qualifier("whisper")
                               VoiceRecognitionService voiceRecognitionService) {
        this.wordsToNumbersService = wordsToNumbersService;
        this.voiceRecognitionService = voiceRecognitionService;
    }

    public String processVoiceMessage(VoiceMessageDto voiceMessage) {
        String textMessage = voiceRecognitionService.voiceToText(voiceMessage);
        return wordsToNumbersService.wordsToNumbers(textMessage.trim());
    }
}
