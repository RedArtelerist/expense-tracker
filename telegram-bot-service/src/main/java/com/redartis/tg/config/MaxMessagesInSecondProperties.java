package com.redartis.tg.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "max-mailing-messages")
@Getter
@Setter
public class MaxMessagesInSecondProperties {
    private long maxMessagesOfAnnouncePerSecond;
}
