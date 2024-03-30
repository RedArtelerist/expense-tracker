package com.redartis.expense.mapper;

import com.redartis.dto.telegram.ChatMemberDto;
import com.redartis.expense.model.User;
import com.redartis.expense.security.dto.TelegramAuthRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User mapChatMemberDtoToUser(ChatMemberDto chatMemberDto) {
        return User.builder()
                .id(chatMemberDto.userId())
                .username(chatMemberDto.username())
                .firstName(chatMemberDto.firstName())
                .lastName(chatMemberDto.lastName())
                .build();
    }

    public User mapTelegramAuthToUser(TelegramAuthRequest telegramAuthRequest) {
        return User.builder()
                .id(telegramAuthRequest.getId())
                .username(telegramAuthRequest.getUsername())
                .firstName(telegramAuthRequest.getFirstName())
                .lastName(telegramAuthRequest.getLastName())
                .photoUrl(telegramAuthRequest.getPhotoUrl())
                .authDate(telegramAuthRequest.getAuthDate())
                .build();
    }
}
