package com.redartis.recognizerservice.service.voice;

import com.redartis.dto.telegram.VoiceMessageDto;

public interface VoiceRecognitionService {
    String voiceToText(VoiceMessageDto voiceMessageData);
}
