package com.redartis.expense.controller.rest;

import com.redartis.dto.admin.BugReportDto;
import com.redartis.expense.service.BugReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BugReportService bugReportService;

    @GetMapping("/bugreports")
    public List<BugReportDto> getBugReports() {
        return bugReportService.getBugReports();
    }

    @DeleteMapping("/bugreports/{id}")
    public void deleteBugReport(@PathVariable Long id) {
        bugReportService.deleteBugReport(id);
    }

    @GetMapping("/bugreports/{id}")
    public BugReportDto getBugReport(@PathVariable Long id) {
        return bugReportService.getBugReport(id);
    }
}
