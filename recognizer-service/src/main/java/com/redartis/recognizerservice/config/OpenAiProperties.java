package com.redartis.recognizerservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open-ai")
@Getter
@Setter
public class OpenAiProperties {
    private String token;
    private String model;
}
