package com.redartis.recognizerservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "deepgram")
@Getter
@Setter
public class DeepgramProperties {
    private String token;
    private String model;
}
