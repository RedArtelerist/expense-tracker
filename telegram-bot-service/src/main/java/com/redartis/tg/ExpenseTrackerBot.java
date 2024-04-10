package com.redartis.tg;

import com.redartis.dto.account.BackupUserDataDto;
import com.redartis.tg.service.BackupService;
import com.redartis.tg.service.ExpenseRequestService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class ExpenseTrackerBot implements SpringLongPollingBot,
        LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;

    @Autowired
    private BackupService backupService;
    @Autowired
    private ExpenseRequestService expenseRequestService;

    public ExpenseTrackerBot(@Value("${bot.token}") String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        System.out.println("MyAmazingBot successfully started!");
        return this;
    }

    @Override
    @SneakyThrows
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message receivedMessage = update.getMessage();
            Long chatId = receivedMessage.getChatId();

            BackupUserDataDto backup = expenseRequestService.getBackup(chatId);

            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(backup.toString())
                    .build());
        }
    }
}
