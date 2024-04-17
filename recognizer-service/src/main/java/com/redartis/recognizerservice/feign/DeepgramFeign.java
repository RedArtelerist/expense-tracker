package com.redartis.recognizerservice.feign;

import com.redartis.recognizerservice.dto.deepgram.ResponseResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "deepgram-feign", url = "https://api.deepgram.com/v1")
public interface DeepgramFeign {
    @PostMapping("/listen")
    ResponseResultDto recognizeVoiceMessage(
            @RequestPart("url") String fileUrl,
            @RequestParam String model,
            @RequestParam String language,
            @RequestHeader("Authorization") String token);
}
