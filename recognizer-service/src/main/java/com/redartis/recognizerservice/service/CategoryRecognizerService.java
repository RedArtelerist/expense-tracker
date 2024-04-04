package com.redartis.recognizerservice.service;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.category.KeywordIdDto;
import com.redartis.dto.transaction.TransactionDto;
import com.redartis.recognizerservice.feign.ExpenseTrackerFeign;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryRecognizerService {
    private final ExpenseTrackerFeign expenseTrackerFeign;

    private float calculateLevenshteinDistance(String strOne, String strTwo) {
        strOne = strOne.toLowerCase();
        strTwo = strTwo.toLowerCase();
        float maxLength = Integer.max(strOne.length(), strTwo.length());
        if (maxLength > 0) {
            Integer levenshteinDistance = LevenshteinDistance.getDefaultInstance()
                    .apply(strOne, strTwo);
            return (maxLength - levenshteinDistance) / maxLength;
        }
        return 0.0f;
    }

    public CategoryDto recognizeCategory(String message, List<CategoryDto> categories) {
        if (categories.isEmpty()) {
            return null;
        }
        CategoryDto[] mostSuitableCategory = {categories.getFirst()};
        float[] maxLevenshteinDistance = {0};

        categories.forEach(category -> category.keywords().add(
                KeywordIdDto.builder()
                        .name(category.name())
                        .build()));

        categories.forEach(category -> category.keywords().forEach(keyword -> {
            float currentValue = calculateLevenshteinDistance(message, keyword.name());
            if (currentValue > maxLevenshteinDistance[0]) {
                mostSuitableCategory[0] = category;
                maxLevenshteinDistance[0] = currentValue;
            }
        }));
        return mostSuitableCategory[0];
    }

    public void sendTransactionWithSuggestedCategory(
            String message,
            List<CategoryDto> categories,
            UUID transactionId) {
        Long suggestedCategoryId = recognizeCategory(message, categories).id();
        TransactionDto transactionDTO = TransactionDto.builder()
                .suggestedCategoryId(suggestedCategoryId)
                .id(transactionId).build();
        expenseTrackerFeign.editTransaction(transactionDTO);
    }
}
