package com.redartis.tg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.redartis.dto.account.BackupUserDataDto;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupUserDataService {
    private final ExpenseRequestService expenseRequestService;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public String createBackupFileToRemoteInChatUser(Long chatId, Long userId) {
        BackupUserDataDto backupUserData = expenseRequestService.getBackup(chatId, userId);
        return createBackupFile(userId, backupUserData);
    }

    public Map<Long, String> createBackupFilesForAllUsersInChat(Long chatId) {
        return expenseRequestService.getBackupsForGroupAccount(chatId)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> createBackupFile(entry.getKey(), entry.getValue())
                ));
    }

    private String createBackupFile(Long userId, BackupUserDataDto backupUserData) {
        String fileName = String.format("%d-%s-backup.json", userId, LocalDate.now());
        try {
            mapper.writeValue(new File(fileName), backupUserData);
        } catch (IOException e) {
            log.error("Can't create backup file", e);
        }
        return fileName;
    }
}
