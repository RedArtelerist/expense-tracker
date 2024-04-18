package com.redartis.expense.service;

import com.redartis.dto.admin.BugReportDto;
import com.redartis.expense.exception.BugReportNotFoundException;
import com.redartis.expense.mapper.BugReportMapper;
import com.redartis.expense.model.BugReport;
import com.redartis.expense.model.User;
import com.redartis.expense.repository.BugReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BugReportService {
    private final BugReportRepository bugReportRepository;
    private final UserService userService;
    private final BugReportMapper bugReportMapper;

    public void saveBugReport(BugReportDto bugReportDto, Long userid) {
        User user = userService.getUserById(userid);
        BugReport bugReport = bugReportMapper.maptoBugReport(bugReportDto);
        bugReport.setUser(user);
        bugReportRepository.save(bugReport);
    }

    public List<BugReportDto> getBugReports() {
        return bugReportRepository.findAllOrderedByIdDesc().stream()
                .map(bugReportMapper::mapToDto)
                .toList();
    }

    public void deleteBugReport(Long id) {
        bugReportRepository.deleteById(id);
    }

    public BugReportDto getBugReport(Long id) {
        return bugReportMapper.mapToDto(findBugReportById(id));
    }

    public BugReport findBugReportById(Long id) {
        return bugReportRepository.findById(id).orElseThrow(
                () -> new BugReportNotFoundException("Can't find bug report with id: " + id));
    }
}
