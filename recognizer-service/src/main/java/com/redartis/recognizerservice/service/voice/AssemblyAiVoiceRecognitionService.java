package com.redartis.recognizerservice.service.voice;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.SpeechModel;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptLanguageCode;
import com.assemblyai.api.resources.transcripts.types.TranscriptOptionalParams;
import com.redartis.dto.telegram.VoiceMessageDto;
import com.redartis.recognizerservice.config.AssemblyAiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("assembly-ai")
public class AssemblyAiVoiceRecognitionService implements VoiceRecognitionService {
    private final AssemblyAiProperties assemblyAiProperties;

    @Override
    public String voiceToText(VoiceMessageDto voiceMessageData) {
        AssemblyAI client = AssemblyAI.builder()
                .apiKey(assemblyAiProperties.getToken())
                .build();

        var params = TranscriptOptionalParams.builder()
                .speechModel(SpeechModel.BEST)
                .languageCode(TranscriptLanguageCode.UK)
                .build();

        Transcript transcript = client.transcripts()
                .transcribe(voiceMessageData.fileUrl(), params);
        return transcript.getText().orElse("");
    }
}
