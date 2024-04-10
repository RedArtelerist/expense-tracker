package com.redartis.tg.mapper;

import com.redartis.dto.telegram.ChatMemberDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class ChatMemberMapper {
    public ChatMemberDto mapUserToChatMemberDto(Long chatId, User user) {
        return ChatMemberDto.builder()
                .chatId(chatId)
                .userId(user.getId())
                .username(user.getUserName())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .build();
    }
}
