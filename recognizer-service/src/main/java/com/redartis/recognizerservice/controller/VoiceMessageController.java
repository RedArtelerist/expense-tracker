package com.redartis.recognizerservice.controller;

import com.redartis.dto.telegram.VoiceMessageDto;
import com.redartis.recognizerservice.service.VoiceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoiceMessageController {
    private final VoiceMessageService voiceMessageService;

    @PostMapping("/voice")
    public String processVoiceMessage(@RequestBody VoiceMessageDto voiceMessage) {
        return voiceMessageService.processVoiceMessage(voiceMessage);
    }
}
