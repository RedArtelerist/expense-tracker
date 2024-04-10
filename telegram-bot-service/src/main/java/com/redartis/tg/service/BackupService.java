package com.redartis.tg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.redartis.dto.account.BackupUserDataDto;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupService {
    private final ExpenseRequestService expenseRequestService;

    public String createBackupFileToRemoteInChatUser(Long chatId, Long userId) {
        BackupUserDataDto backupUserData = expenseRequestService.getBackup(chatId);
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String fileName = userId.toString() + "-" + LocalDate.now() + "-backup.json";
        try {
            mapper.writeValue(new File(fileName), backupUserData);
        } catch (IOException e) {
            log.error("Can't create json backup file", e);
        }
        return fileName;
    }
}
