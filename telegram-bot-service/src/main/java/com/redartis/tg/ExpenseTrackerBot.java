package com.redartis.tg;

import static com.redartis.tg.util.TelegramBotAnswer.ACCOUNT_NOT_FOUND_MESSAGE;
import static com.redartis.tg.util.TelegramBotAnswer.INVALID_TRANSACTION_TO_DELETE;
import static com.redartis.tg.util.TelegramBotAnswer.INVALID_UPDATE_TRANSACTION_TEXT;
import static com.redartis.tg.util.TelegramBotAnswer.MERGE_REQUEST_COMPLETED_DEFAULT_TEXT;
import static com.redartis.tg.util.TelegramBotAnswer.MERGE_REQUEST_COMPLETED_TEXT;
import static com.redartis.tg.util.TelegramBotAnswer.REGISTRATION_INFO_TEXT;
import static com.redartis.tg.util.TelegramBotAnswer.SUCCESSFUL_DELETION_TRANSACTION;
import static com.redartis.tg.util.TelegramBotAnswer.SUCCESSFUL_UPDATE_TRANSACTION_TEXT;
import static com.redartis.tg.util.TelegramBotAnswer.TRANSACTION_MESSAGE_INVALID;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.dto.telegram.ChatMemberDto;
import com.redartis.dto.transaction.TransactionMessageDto;
import com.redartis.dto.transaction.TransactionResponseDto;
import com.redartis.tg.constants.Command;
import com.redartis.tg.constants.InlineKeyboardCallback;
import com.redartis.tg.exception.InvalidVoiceDurationException;
import com.redartis.tg.mapper.ChatMemberMapper;
import com.redartis.tg.mapper.TransactionMapper;
import com.redartis.tg.model.TelegramMessage;
import com.redartis.tg.service.BackupUserDataService;
import com.redartis.tg.service.ExpenseRequestService;
import com.redartis.tg.service.MergeRequestService;
import com.redartis.tg.service.TelegramMessageCheckerService;
import com.redartis.tg.service.TelegramMessageService;
import com.redartis.tg.util.TelegramBotUtil;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExpenseTrackerBot implements SpringLongPollingBot,
        LongPollingSingleThreadUpdateConsumer {
    private static final ZoneOffset UA_OFFSET = ZoneOffset.of("+02:00");
    private static final Integer MILLISECONDS_CONVERSION = 1000;
    private static final String COMMAND_TO_DELETE_TRANSACTION = "delete";
    private static final String BLANK_MESSAGE = "";
    private static final Boolean BOT = true;

    @Value("${orchestrator.host}")
    private String webApplicationHost;
    @Value("${bot.token}")
    private String botToken;

    private final ExpenseRequestService expenseRequestService;
    private final TelegramMessageService telegramMessageService;
    private final TelegramMessageCheckerService telegramMessageCheckerService;
    private final MergeRequestService mergeRequestService;
    private final BackupUserDataService backupUserDataService;
    private final ChatMemberMapper chatMemberMapper;
    private final TransactionMapper transactionMapper;
    private final TelegramBotUtil telegramBotUtil;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    @SneakyThrows
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message receivedMessage = update.getMessage();
            Long chatId = receivedMessage.getChatId();

            if (receivedMessage.getLeftChatMember() != null) {
                User remoteUser = receivedMessage.getLeftChatMember();
                String groupName = receivedMessage.getChat().getTitle();

                if (!remoteUser.getIsBot()) {
                    var backupFileName = backupUserDataService.createBackupFileToRemoteInChatUser(
                            chatId,
                            remoteUser.getId(),
                            groupName
                    );
                    telegramBotUtil.sendBuckUpFile(
                            remoteUser.getId().toString(),
                            backupFileName,
                            groupName
                    );

                    ChatMemberDto leftChatMember = chatMemberMapper.mapUserToChatMemberDto(
                            chatId, remoteUser
                    );
                    expenseRequestService.removeChatMemberFromAccount(leftChatMember);
                    log.info("User with id %d left chat with id %d"
                            .formatted(remoteUser.getId(), chatId));
                } else {
                    var backupUsersMap = backupUserDataService
                            .createBackupFilesForAllUsersInChat(chatId, groupName);
                    backupUsersMap.forEach((userId, backup) -> telegramBotUtil.sendBuckUpFile(
                            userId.toString(),
                            backup, groupName)
                    );

                    expenseRequestService.deleteGroupAccount(chatId);
                    mergeRequestService.deleteMergeRequestsByChatId(chatId);
                    telegramMessageService.deleteTransactionMessagesByChatId(chatId);
                    log.info("Bot left chat with id %d and name %s".formatted(chatId, groupName));
                }
            }

            if (!receivedMessage.getNewChatMembers().isEmpty()) {
                var newUsers = telegramBotUtil.getUsersTypes(receivedMessage.getNewChatMembers());

                if (!newUsers.get(BOT).isEmpty()) {
                    telegramBotUtil.sendMessage(chatId, REGISTRATION_INFO_TEXT);
                    telegramBotUtil.sendMergeRequest(chatId);
                } else {
                    expenseRequestService.addNewChatMembersToAccount(newUsers.get(!BOT)
                            .stream()
                            .map(member -> chatMemberMapper.mapUserToChatMemberDto(chatId, member))
                            .toList()
                    );
                }
            }
            botAnswer(receivedMessage);
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long chatId = callbackQuery.getMessage().getChatId();
            Long userId = callbackQuery.getFrom().getId();
            Integer mergeMessageId = mergeRequestService.getMergeRequestByChatId(chatId)
                    .getMessageId();

            if (callbackQuery.getData().equals(
                    InlineKeyboardCallback.MERGE_CATEGORIES.getData())) {
                expenseRequestService.registerGroupAccountAndMergeCategoriesWithoutTransactions(
                        new AccountDataDto(chatId, userId)
                );
                telegramBotUtil.editMessage(mergeMessageId, chatId, MERGE_REQUEST_COMPLETED_TEXT);
            } else if (callbackQuery.getData().equals(
                    InlineKeyboardCallback.MERGE_CATEGORIES_AND_TRANSACTIONS.getData())) {
                expenseRequestService.registerGroupAccountAndMergeWithCategoriesAndTransactions(
                        new AccountDataDto(chatId, userId));
                telegramBotUtil.editMessage(mergeMessageId, chatId, MERGE_REQUEST_COMPLETED_TEXT);
            } else if (callbackQuery.getData().equals(InlineKeyboardCallback.DEFAULT.getData())) {
                expenseRequestService.registerGroupAccount(new AccountDataDto(chatId, userId));
                telegramBotUtil.deleteMessage(mergeMessageId, chatId);
            }

            mergeRequestService.updateMergeRequestCompletionByChatId(chatId);
            telegramBotUtil.sendMessage(chatId, MERGE_REQUEST_COMPLETED_DEFAULT_TEXT);
        }
    }

    private void botAnswer(Message receivedMessage) {
        try {
            Long chatId = receivedMessage.getChatId();
            Long userId = receivedMessage.getFrom().getId();
            Integer messageId = receivedMessage.getMessageId();

            String receivedMessageText = telegramBotUtil.getReceivedMessage(receivedMessage)
                    .toLowerCase();
            Message replyToMessage = receivedMessage.getReplyToMessage();

            long epochMilli = (long) receivedMessage.getDate() * MILLISECONDS_CONVERSION;
            LocalDateTime date = Instant.ofEpochMilli(epochMilli)
                    .atOffset(UA_OFFSET)
                    .toLocalDateTime();

            TransactionMessageDto transactionMessageDto = TransactionMessageDto.builder()
                    .message(receivedMessageText)
                    .userId(userId)
                    .chatId(chatId)
                    .date(date)
                    .build();

            if (telegramMessageCheckerService.isNonTransactionalMessage(receivedMessageText)
                    || receivedMessageText.equals(BLANK_MESSAGE)) {
                return;
            }

            if (replyToMessage != null) {
                TelegramMessage message = telegramMessageService
                        .getTelegramMessageByMessageIdAndChatId(
                                replyToMessage.getMessageId(), chatId
                        );
                if (message == null) {
                    if (!userId.equals(replyToMessage.getFrom().getId())) {
                        telegramBotUtil.sendMessage(chatId, INVALID_UPDATE_TRANSACTION_TEXT);
                        return;
                    }
                    processTransaction(chatId, messageId, transactionMessageDto);
                    return;
                }
                if (!receivedMessageText.equals(COMMAND_TO_DELETE_TRANSACTION)
                        && !receivedMessageText.equalsIgnoreCase(replyToMessage.getText())) {
                    UUID idTransaction = message.getTransactionId();
                    updateTransaction(transactionMessageDto, idTransaction, chatId, messageId);
                    return;
                }
            }

            switch (receivedMessageText) {
                case "/start" -> {
                    telegramBotUtil.sendMessage(
                            chatId,
                            Command.START.getDescription().formatted(webApplicationHost)
                    );
                    expenseRequestService.registerSingleAccount(
                            new AccountDataDto(chatId, userId)
                    );
                }
                case "/web" -> telegramBotUtil.sendMessage(chatId, webApplicationHost);
                case COMMAND_TO_DELETE_TRANSACTION -> {
                    if (replyToMessage != null) {
                        deleteTransaction(replyToMessage, chatId);
                    }
                }
                default -> processTransaction(chatId, messageId, transactionMessageDto);
            }
        } catch (InvalidVoiceDurationException e) {
            log.error(e.getMessage());
            telegramBotUtil.sendMessage(receivedMessage.getChatId(), e.getMessage());
        }
    }

    private void processTransaction(Long chatId,
                                    Integer messageId,
                                    TransactionMessageDto transactionMessageDto) {
        try {
            TransactionResponseDto transactionResponse = expenseRequestService
                    .sendTransaction(transactionMessageDto);
            telegramMessageService.saveTelegramMessage(TelegramMessage.builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .transactionId(transactionResponse.id())
                    .build());
            telegramBotUtil.sendMessage(
                    chatId,
                    transactionMapper.mapTransactionResponseToTelegramMessage(transactionResponse)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e.getMessage().contains("ORCHESTRA_ACCOUNT_NOT_FOUND")) {
                telegramBotUtil.sendMessage(
                        chatId,
                        ACCOUNT_NOT_FOUND_MESSAGE.formatted(webApplicationHost)
                );
            } else {
                telegramBotUtil.sendMessage(chatId, TRANSACTION_MESSAGE_INVALID);
            }
        }
    }

    private void updateTransaction(TransactionMessageDto transactionMessageDto,
                                   UUID transactionId,
                                   Long chatId,
                                   Integer messageId) {
        try {
            TransactionResponseDto transactionResponse = expenseRequestService
                    .submitTransactionForUpdate(transactionMessageDto, transactionId);
            telegramMessageService.saveTelegramMessage(TelegramMessage.builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .transactionId(transactionResponse.id())
                    .build());
            telegramMessageService.deleteTelegramMessageByTransactionId(transactionId);
            String telegramMessage = transactionMapper
                    .mapTransactionResponseToTelegramMessage(transactionResponse);
            telegramBotUtil.sendMessage(
                    chatId,
                    SUCCESSFUL_UPDATE_TRANSACTION_TEXT + telegramMessage
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            telegramBotUtil.sendMessage(chatId, TRANSACTION_MESSAGE_INVALID);
        }
    }

    private void deleteTransaction(Message replyToMessage, Long chatId) {
        try {
            telegramMessageService.deleteTransactionById(replyToMessage.getMessageId(), chatId);
            telegramBotUtil.sendMessage(chatId, SUCCESSFUL_DELETION_TRANSACTION);
        } catch (Exception e) {
            log.error(e.getMessage());
            telegramBotUtil.sendMessage(chatId, INVALID_TRANSACTION_TO_DELETE);
        }
    }
}
