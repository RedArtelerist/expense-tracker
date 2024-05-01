package com.redartis.tg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.redartis.dto.account.BackupUserDataDto;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupUserDataService {
    private static final DateTimeFormatter BACKUP_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd.hh-mm");

    private final ExpenseRequestService expenseRequestService;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public String createBackupFileToRemoteInChatUser(Long chatId, Long userId, String groupName) {
        BackupUserDataDto backupUserData = expenseRequestService.getBackup(chatId, userId);
        return createBackupFile(groupName, backupUserData);
    }

    public Map<Long, String> createBackupFilesForAllUsersInChat(Long chatId, String groupName) {
        return expenseRequestService.getBackupsForGroupAccount(chatId)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> createBackupFile(groupName, entry.getValue())
                ));
    }

    private String createBackupFile(String groupName, BackupUserDataDto backupUserData) {
        String fileName = String.format(
                "%s-%s-backup.json",
                LocalDateTime.now().format(BACKUP_DATE_TIME_FORMAT),
                groupName
        );
        try {
            mapper.writeValue(new File(fileName), backupUserData);
        } catch (IOException e) {
            log.error("Can't create backup file", e);
        }
        return fileName;
    }
}
