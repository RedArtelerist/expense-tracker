package com.redartis.expense.controller.rest;

import com.redartis.dto.transaction.TransactionDefineDto;
import com.redartis.dto.transaction.TransactionDto;
import com.redartis.dto.transaction.TransactionMessageDto;
import com.redartis.dto.transaction.TransactionResponseDto;
import com.redartis.expense.mapper.TransactionMapper;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.service.TransactionDefineService;
import com.redartis.expense.service.TransactionProcessingService;
import com.redartis.expense.service.TransactionService;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionProcessingService transactionProcessingService;
    private final TransactionDefineService transactionDefineService;
    private final TelegramUtils telegramUtils;
    private final TransactionMapper transactionMapper;

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
    public List<TransactionDto> getTransactionsByPeriodAndCategory(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String category) {
        return transactionService.getTransactionsByPeriodAndCategory(year, month, category);
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

    @PostMapping
    public TransactionResponseDto processTransaction(
            @RequestBody TransactionMessageDto transactionMessage,
            Principal principal) {
        Transaction transaction = transactionProcessingService.validateAndProcessTransaction(
                transactionMessage,
                principal
        );
        transactionService.saveTransaction(transaction);
        transactionProcessingService.suggestCategoryToProcessedTransaction(
                transactionMessage,
                transaction.getId()
        );
        return transactionMapper.mapTransactionToTelegramResponse(transaction);
    }

    @PatchMapping
    public ResponseEntity<String> editTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionService.enrichTransactionWithSuggestedCategory(
                transactionDto
        );
        transactionService.saveTransaction(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/define")
    public ResponseEntity<String> define(@RequestBody TransactionDefineDto transactionDefineDto) {
        transactionDefineService.defineTransactionCategoryByTransactionIdAndCategoryId(
                transactionDefineDto.transactionId(),
                transactionDefineDto.categoryId()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/undefine")
    public ResponseEntity<String> undefine(
            @RequestBody TransactionDefineDto transactionDefineDto) {
        transactionDefineService.undefineTransactionCategoryAndKeywordCategory(
                transactionDefineDto.transactionId()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public void updateTransaction(@RequestBody TransactionDto transactionDto) {
        transactionService.updateTransaction(transactionDto);
    }

    @PutMapping("/{id}")
    public TransactionResponseDto updateTransaction(
            @RequestBody TransactionMessageDto transactionMessage,
            @PathVariable("id") UUID id) {
        return transactionService.updateTransactionFromTelegramChat(transactionMessage, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionById(@PathVariable("id") UUID id) {
        transactionService.deleteTransactionById(id);
    }
}
