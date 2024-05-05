package com.redartis.expense.controller.rest;

import com.redartis.dto.account.BackupUserDataDto;
import com.redartis.expense.annotations.OnlyServiceUse;
import com.redartis.expense.service.BackupUserDataService;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@Slf4j
@RequiredArgsConstructor
public class SettingsController {
    private final BackupUserDataService backupUserDataService;

    private final TelegramUtils telegramUtils;

    @GetMapping("/backup")
    public BackupUserDataDto getUserBackupData(Principal principal) {
        return backupUserDataService.createBackupUserData(telegramUtils.getTelegramId(principal));
    }

    @OnlyServiceUse
    @GetMapping("/backup/{chatId}/{userId}")
    public BackupUserDataDto getBackupDataFromRemovedUser(@PathVariable Long chatId,
                                                          @PathVariable Long userId) {
        return backupUserDataService.createBackupForGroupMember(chatId, userId);
    }

    @OnlyServiceUse
    @GetMapping("/backup/{chatId}")
    public Map<Long, BackupUserDataDto> getBackupForGroupAccount(@PathVariable Long chatId) {
        return backupUserDataService.createBackupForGroupAccount(chatId);
    }

    @PostMapping("/backup/read")
    public ResponseEntity<HttpStatus> readBackupFile(
            Principal principal,
            @RequestBody BackupUserDataDto backupUserDataDto) {
        backupUserDataService.writingDataFromBackupFile(
                backupUserDataDto,
                telegramUtils.getTelegramId(principal)
        );
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
