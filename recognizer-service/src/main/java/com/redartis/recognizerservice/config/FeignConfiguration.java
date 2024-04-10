package com.redartis.recognizerservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    private static final String HEADER_NAME = "X-INTERNAL-KEY";

    @Value("${authorization-header.header-value}")
    private String headerValue;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header(HEADER_NAME, headerValue);
    }
}
