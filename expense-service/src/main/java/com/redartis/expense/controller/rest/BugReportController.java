package com.redartis.expense.controller.rest;

import com.redartis.dto.admin.BugReportDto;
import com.redartis.expense.service.BugReportService;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BugReportController {
    private final TelegramUtils telegramUtils;
    private final BugReportService bugReportService;

    @PostMapping("/bugreport")
    public ResponseEntity<HttpStatus> saveBugReport(@RequestBody BugReportDto bugReportDto,
                                                    Principal principal) {
        Long userId = telegramUtils.getTelegramId(principal);
        bugReportService.saveBugReport(bugReportDto, userId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
