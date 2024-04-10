package com.redartis.expense.mapper;

import com.redartis.dto.analytics.AnalyticsMonthlyReportForYearDto;
import com.redartis.dto.analytics.AnalyticsMonthlyReportForYearWithSharesDto;
import com.redartis.dto.analytics.MonthlyAnalyticsByCategoryDto;
import com.redartis.expense.util.NumericalUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsMapper {
    private static final int FIRST_MONTH = 1;
    private static final int MONTHS_IN_YEAR = 12;

    public List<AnalyticsMonthlyReportForYearDto> mapToAnalyticsMonthlyReport(
            List<MonthlyAnalyticsByCategoryDto> objects) {
        Map<String, Map<Integer, BigDecimal>> groupedByCategoryName = objects.stream()
                .collect(Collectors.groupingBy(
                        MonthlyAnalyticsByCategoryDto::categoryName,
                        Collectors.toMap(
                                MonthlyAnalyticsByCategoryDto::month,
                                MonthlyAnalyticsByCategoryDto::amount,
                                (amount1, amount2) -> amount1
                        )
                ));

        return groupedByCategoryName.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    var monthlyAnalytics = IntStream.rangeClosed(FIRST_MONTH, MONTHS_IN_YEAR)
                            .boxed()
                            .collect(Collectors.toMap(
                                    Function.identity(),
                                    month -> NumericalUtils.roundAmount(
                                            entry.getValue().getOrDefault(month, BigDecimal.ZERO)
                                    )
                            ));
                    return new AnalyticsMonthlyReportForYearDto(categoryName, monthlyAnalytics);
                })
                .toList();
    }

    public List<AnalyticsMonthlyReportForYearWithSharesDto> mapToAnalyticsMonthlyReportWithShares(
            List<MonthlyAnalyticsByCategoryDto> statistics) {
        var analyticsMonthlyReportsWithoutShares = mapToAnalyticsMonthlyReport(statistics);
        var total = getTotalExpenseByYearAndMonths(analyticsMonthlyReportsWithoutShares);

        return analyticsMonthlyReportsWithoutShares.stream()
                .map(report -> createAnalyticsMonthlyReportWithShares(report, total))
                .toList();
    }

    private Map<Integer, BigDecimal> getTotalExpenseByYearAndMonths(
            List<AnalyticsMonthlyReportForYearDto> results) {
        return IntStream.rangeClosed(FIRST_MONTH, MONTHS_IN_YEAR)
                .boxed()
                .collect(Collectors.toMap(
                        Function.identity(),
                        month -> results.stream()
                                .map(dto -> dto.monthlyAnalytics().get(month))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ));
    }

    private AnalyticsMonthlyReportForYearWithSharesDto createAnalyticsMonthlyReportWithShares(
            AnalyticsMonthlyReportForYearDto report, Map<Integer, BigDecimal> total) {
        Map<Integer, Double> shareOfMonthlyExpenses = report.monthlyAnalytics().entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().doubleValue() != 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateShare(entry.getValue(), total.get(entry.getKey()))
                ));

        IntStream.rangeClosed(1, 12).forEach(month -> {
            report.monthlyAnalytics().putIfAbsent(month, BigDecimal.ZERO);
            shareOfMonthlyExpenses.putIfAbsent(month, 0d);
        });

        return new AnalyticsMonthlyReportForYearWithSharesDto(
                report.categoryName(),
                report.monthlyAnalytics(),
                shareOfMonthlyExpenses
        );
    }

    private double calculateShare(BigDecimal amount, BigDecimal totalAmount) {
        if (totalAmount.doubleValue() == 0) {
            return 0;
        }
        double total = totalAmount.doubleValue();
        return 1 - ((total - amount.doubleValue()) / total);
    }
}
