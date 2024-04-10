package com.redartis.expense.controller.rest;

import com.redartis.dto.analytics.AnalyticsDataDto;
import com.redartis.dto.analytics.AnalyticsDataMonthDto;
import com.redartis.dto.analytics.AnalyticsMonthlyReportForYearDto;
import com.redartis.dto.analytics.AnalyticsMonthlyReportForYearWithSharesDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.service.AccountService;
import com.redartis.expense.service.AnalyticService;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@Slf4j
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticService analyticService;
    private final TelegramUtils telegramUtils;
    private final AccountService accountService;

    @GetMapping("/totalCategorySums/{type}")
    public List<AnalyticsDataDto> getAnalyticsTotalCategorySums(
            Principal principal,
            @PathVariable("type") Type type) {
        Long telegramId = telegramUtils.getTelegramId(principal);
        return analyticService.getTotalCategorySumsForAnalytics(telegramId, type);
    }

    @GetMapping("/totalIncomeOutcome/{year}")
    public List<AnalyticsDataMonthDto> getIncomeOutcomePerMonth(
            Principal principal,
            @PathVariable("year") int year) {
        Long telegramId = telegramUtils.getTelegramId(principal);
        return analyticService.getTotalIncomeOutcomePerMonth(telegramId, year);
    }

    @GetMapping("/available-years")
    public List<Integer> getAvailableYears(Principal principal) {
        return analyticService.findAvailableYears(telegramUtils.getTelegramId(principal));
    }

    @GetMapping("/income/{year}")
    public List<AnalyticsMonthlyReportForYearDto> getYearIncomeStatistics(
            Principal principal,
            @PathVariable("year") Integer year) {
        Long telegramId = telegramUtils.getTelegramId(principal);
        return analyticService.findMonthlyIncomeStatisticsForYearByAccountId(telegramId, year);
    }

    @GetMapping("/total/{year}")
    public List<AnalyticsMonthlyReportForYearWithSharesDto> getYearTotalStatistics(
            Principal principal,
            @PathVariable("year") Integer year) {
        Long telegramId = telegramUtils.getTelegramId(principal);
        return analyticService.findMonthlyTotalStatisticsByAccountId(telegramId, year);
    }
}
