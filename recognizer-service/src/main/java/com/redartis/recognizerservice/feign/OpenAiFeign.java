package com.redartis.recognizerservice.feign;

import com.redartis.recognizerservice.dto.WhisperResponseDto;
import java.io.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "openai-feign", url = "https://api.openai.com/v1/audio")
public interface OpenAiFeign {
    @RequestMapping(value = "/transcriptions",
            method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    WhisperResponseDto recognizeVoiceMessage(
            @RequestPart File file,
            @RequestPart(value = "model") String model,
            @RequestPart(value = "language") String language,
            @RequestPart(value = "response_format") String responseFormat,
            @RequestHeader("Authorization") String token);
}
