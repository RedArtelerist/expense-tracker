package com.redartis.recognizerservice.service.voice;

import com.redartis.dto.telegram.VoiceMessageDto;
import com.redartis.recognizerservice.config.DeepgramProperties;
import com.redartis.recognizerservice.exception.VoiceRecognitionException;
import com.redartis.recognizerservice.feign.DeepgramFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("deepgram")
public class DeepgramVoiceRecognitionService implements VoiceRecognitionService {
    private static final String BEARER_TOKEN = "Token ";
    private static final String LANGUAGE_CODE = "uk";

    private final DeepgramProperties deepgramProperties;
    private final DeepgramFeign deepgramFeign;

    @Override
    public String voiceToText(VoiceMessageDto voiceMessageData) {
        try {
            String token = BEARER_TOKEN + deepgramProperties.getToken();

            var response = deepgramFeign.recognizeVoiceMessage(
                    voiceMessageData.fileUrl(),
                    deepgramProperties.getModel(),
                    LANGUAGE_CODE,
                    token
            );

            return response.result()
                    .channels()
                    .getFirst()
                    .transcriptResults()
                    .getFirst()
                    .transcript();
        } catch (Exception e) {
            throw new VoiceRecognitionException("Deepgram: can't recognize voice message", e);
        }
    }
}
