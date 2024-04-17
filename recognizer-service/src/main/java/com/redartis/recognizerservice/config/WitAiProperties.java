package com.redartis.recognizerservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wit-ai")
@Getter
@Setter
public class WitAiProperties {
    private String url;
    private String token;
    private String version;
}
