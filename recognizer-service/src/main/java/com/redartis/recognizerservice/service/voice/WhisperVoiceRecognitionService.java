package com.redartis.recognizerservice.service.voice;

import com.redartis.dto.telegram.VoiceMessageDto;
import com.redartis.recognizerservice.config.OpenAiProperties;
import com.redartis.recognizerservice.exception.VoiceRecognitionException;
import com.redartis.recognizerservice.feign.OpenAiFeign;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("whisper")
public class WhisperVoiceRecognitionService implements VoiceRecognitionService {
    private static final String BEARER_TOKEN = "Bearer ";
    private static final String RESPONSE_FORMAT_JSON = "json";
    private static final String LANGUAGE_CODE = "uk";

    private final OpenAiProperties openAiProperties;
    private final OpenAiFeign openAiFeign;

    @Override
    public String voiceToText(VoiceMessageDto voiceMessageData) {
        File voiceFile = new File("voice.oga");
        try {
            try (FileOutputStream outputStream = new FileOutputStream(voiceFile)) {
                outputStream.write(voiceMessageData.voiceMessageBytes());
            }

            String token = BEARER_TOKEN + openAiProperties.getToken();

            var response = openAiFeign.recognizeVoiceMessage(
                    voiceFile,
                    openAiProperties.getModel(),
                    LANGUAGE_CODE,
                    RESPONSE_FORMAT_JSON,
                    token
            );

            return response.text().trim();
        } catch (IOException e) {
            throw new VoiceRecognitionException("Whisper: can't recognize voice message", e);
        } finally {
            voiceFile.delete();
        }
    }
}
