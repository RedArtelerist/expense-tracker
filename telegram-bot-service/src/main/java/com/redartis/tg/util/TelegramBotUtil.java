package com.redartis.tg.util;

import static com.redartis.tg.util.TelegramBotAnswer.BACKUP_MESSAGE;
import static com.redartis.tg.util.TelegramBotAnswer.MERGE_REQUEST_TEXT;

import com.redartis.tg.service.MergeRequestService;
import com.redartis.tg.service.VoiceMessageProcessingService;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBotUtil {
    private static final String BLANK_MESSAGE = "";

    private final TelegramClient telegramClient;
    private final VoiceMessageProcessingService voiceMessageProcessingService;
    private final MergeRequestService mergeRequestService;
    private final InlineKeyboardMarkupUtil inlineKeyboardMarkupUtil;

    public void sendMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId.toString(), messageText);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void sendMergeRequest(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId.toString(), MERGE_REQUEST_TEXT);
        message.setReplyMarkup(inlineKeyboardMarkupUtil.generateMergeRequestMarkup());
        Message mergeRequestMessage = telegramClient.execute(message);
        mergeRequestService.saveMergeRequestMessage(mergeRequestMessage);
    }

    @SneakyThrows
    public void editMessage(Integer messageId, Long chatId, String message) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(message)
                .build();
        telegramClient.execute(editMessageText);
    }

    @SneakyThrows
    public void deleteMessage(Integer messageId, Long chatId) {
        DeleteMessage message = DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
        telegramClient.execute(message);
    }

    public void sendBuckUpFile(String userId, String fileName, String groupName) {
        File file = new File(fileName);
        SendDocument sendDocument = SendDocument.builder()
                .chatId(userId)
                .document(new InputFile(file))
                .caption(BACKUP_MESSAGE.formatted(groupName))
                .build();
        try {
            telegramClient.execute(sendDocument);
            file.delete();
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public String getReceivedMessage(Message message) {
        String receivedMessageText = BLANK_MESSAGE;
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        if (message.hasVoice()) {
            log.info("User with id " + userId + " and chatId " + chatId + " sending voice");
            receivedMessageText = voiceMessageProcessingService.processVoiceMessage(
                    message.getVoice(),
                    userId,
                    chatId
            );
            log.info("Recognition result of user with id %d and chatId %d is: %s"
                    .formatted(userId, chatId, receivedMessageText));
        } else if (message.hasText()) {
            receivedMessageText = message.getText();
        }
        return receivedMessageText;
    }

    public Map<Boolean, List<User>> getUsersTypes(List<User> newUsers) {
        return newUsers.stream()
                .collect(Collectors.partitioningBy(User::getIsBot));
    }
}
