package com.redartis.expense.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionDefineService {
    private final KeywordService keywordService;
    private final TransactionService transactionService;

    @Transactional
    public void defineTransactionCategoryByTransactionIdAndCategoryId(
            UUID transactionId,
            Long categoryId) {
        transactionService.setCategoryForAllUndefinedTransactionsWithSameKeywords(
                transactionId,
                categoryId
        );
        keywordService.associateTransactionsKeywordWithCategory(transactionId, categoryId);
    }

    @Transactional
    public void undefineTransactionCategoryAndKeywordCategory(UUID transactionId) {
        transactionService.removeCategoryFromTransactionsWithSameMessage(transactionId);
        keywordService.removeCategoryFromKeywordByTransactionId(transactionId);
    }
}
