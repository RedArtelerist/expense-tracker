package com.redartis.tg.feign;

import com.redartis.dto.telegram.VoiceMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "recognizer-service")
public interface RecognizerFeign {
    @PostMapping("/voice")
    String recognizeVoiceMessage(@RequestBody VoiceMessageDto voiceMessageDto);
}
