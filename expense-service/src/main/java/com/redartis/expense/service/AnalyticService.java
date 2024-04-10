package com.redartis.expense.service;

import com.redartis.dto.analytics.AnalyticsDataDto;
import com.redartis.dto.analytics.AnalyticsDataMonthDto;
import com.redartis.dto.analytics.AnalyticsMonthlyReportForYearDto;
import com.redartis.dto.analytics.AnalyticsMonthlyReportForYearWithSharesDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.mapper.AnalyticsMapper;
import com.redartis.expense.repository.CategoryRepository;
import com.redartis.expense.repository.TransactionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final AccountService accountService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final AnalyticsMapper analyticsMapper;

    public List<AnalyticsDataDto> getTotalCategorySumsForAnalytics(Long userId, Type type) {
        Long accountId = accountService.getAccountByUserId(userId).getId();
        List<AnalyticsDataDto> analyticsDataList = categoryRepository
                .findMediumAmountOfAllCategoriesByAccountIdAndType(accountId, type);
        return analyticsDataList.stream()
                .filter(analyticsData -> analyticsData.mediumAmountOfTransactions() != null)
                .map(dto -> new AnalyticsDataDto(
                        dto.categoryId(),
                        dto.categoryName(),
                        Math.round(dto.mediumAmountOfTransactions().doubleValue())
                ))
                .toList();
    }

    public List<Integer> findAvailableYears(Long telegramId) {
        Long accountId = accountService.getAccountByUserId(telegramId).getId();
        return transactionRepository.findAvailableYearsForAccountByAccountId(accountId);
    }

    public List<AnalyticsMonthlyReportForYearDto> findMonthlyIncomeStatisticsForYearByAccountId(
            Long telegramId,
            Integer year) {
        Long accountId = accountService.getAccountByUserId(telegramId).getId();
        var statistics = transactionRepository.findMonthlyStatisticsByYearAndAccountIdAndType(
                accountId,
                year,
                Type.INCOME
        );
        return analyticsMapper.mapToAnalyticsMonthlyReport(statistics);
    }

    public List<AnalyticsMonthlyReportForYearWithSharesDto> findMonthlyTotalStatisticsByAccountId(
            Long telegramId,
            Integer year) {
        Long accountId = accountService.getAccountByUserId(telegramId).getId();
        var statistics = transactionRepository.findMonthlyStatisticsByYearAndAccountIdAndType(
                accountId,
                year,
                Type.EXPENSE
        );
        return analyticsMapper.mapToAnalyticsMonthlyReportWithShares(statistics);
    }

    public List<AnalyticsDataMonthDto> getTotalIncomeOutcomePerMonth(Long telegramId, int year) {
        Long accountId = userService.getUserById(telegramId).getAccount().getId();
        var list = transactionRepository.getTotalIncomeOutcomePerMonth(accountId, year);
        return list.stream()
                .map(it -> new AnalyticsDataMonthDto(
                        it.getMonth(),
                        it.getTotalIncome(),
                        it.getTotalExpense()
                ))
                .toList();
    }
}
