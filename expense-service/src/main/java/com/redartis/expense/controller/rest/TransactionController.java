package com.redartis.expense.controller.rest;

import com.redartis.dto.transaction.TransactionDto;
import com.redartis.expense.service.TransactionService;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TelegramUtils telegramUtils;

    @GetMapping("/count")
    public long getTransactionsCount() {
        return transactionService.getTransactionsCount();
    }

    @GetMapping
    public List<TransactionDto> getTransactions(Principal principal) {
        return transactionService.findTransactionsByUserIdWithoutCategories(
                telegramUtils.getTelegramId(principal)
        );
    }

    @GetMapping("/history/{id}")
    public TransactionDto getTransactionById(@PathVariable("id") UUID id) {
        return transactionService.findTransactionById(id);
    }

    @GetMapping("/info")
    public List<TransactionDto> getTransactionsListByPeriodAndCategory(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam long categoryId) {
        return transactionService.getTransactionsByPeriodAndCategory(year, month, categoryId);
    }

    @GetMapping("/history")
    public List<TransactionDto> getTransactionsHistory(
            Principal principal,
            @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {
        return transactionService.findTransactionsByUserIdLimited(
                telegramUtils.getTelegramId(principal), pageSize, pageNumber
        );
    }
}
