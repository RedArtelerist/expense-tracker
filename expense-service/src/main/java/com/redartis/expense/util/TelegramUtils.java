package com.redartis.expense.util;

import com.redartis.expense.security.dto.JwtAuthentication;
import java.security.Principal;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TelegramUtils {

    @Value("${telegram.bot.name}")
    private String telegramBotName;

    public Long getTelegramId(Principal principal) {
        return ((JwtAuthentication) principal).getTelegramId();
    }
}
