package com.redartis.expense.service;

import com.redartis.expense.model.Category;
import com.redartis.expense.model.Keyword;
import com.redartis.expense.model.KeywordId;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.repository.KeywordRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public void saveKeyword(Keyword keyword) {
        keywordRepository.save(keyword);
    }

    @Transactional
    public void deleteKeyword(KeywordId keywordId) {
        keywordRepository.deleteByKeywordId(keywordId);
    }

    public void associateTransactionsKeywordWithCategory(UUID transactionId, Long categoryId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        Category category = categoryService.getCategoryByIdWithAccount(categoryId);

        Keyword keyword = new Keyword();
        keyword.setKeywordId(new KeywordId(
                transaction.getMessage(),
                category.getAccount().getId())
        );
        keyword.setCategory(category);
        saveKeyword(keyword);
    }

    public void removeCategoryFromKeywordByTransactionId(UUID transactionId) {
        Transaction transaction = transactionService.getTransactionByIdWithAccount(transactionId);
        var keywordId = new KeywordId(transaction.getMessage(), transaction.getAccount().getId());
        keywordRepository.removeCategoryId(keywordId);
    }
}
