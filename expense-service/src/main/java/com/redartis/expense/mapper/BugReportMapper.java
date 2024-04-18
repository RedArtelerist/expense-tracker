package com.redartis.expense.mapper;

import com.redartis.dto.admin.BugReportDto;
import com.redartis.expense.model.BugReport;
import org.springframework.stereotype.Component;

@Component
public class BugReportMapper {
    public BugReport maptoBugReport(BugReportDto bugReportDto) {
        return BugReport.builder()
                .report(bugReportDto.report())
                .localDateTime(bugReportDto.localDateTime())
                .build();
    }

    public BugReportDto mapToDto(BugReport bugReport) {
        return BugReportDto.builder()
                .id(bugReport.getId())
                .report(bugReport.getReport())
                .localDateTime(bugReport.getLocalDateTime())
                .userId(bugReport.getUser().getId())
                .username(bugReport.getUser().getUsername())
                .build();
    }
}
