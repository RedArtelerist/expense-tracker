package com.redartis.expense.service;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.transaction.TransactionAmountAndCommentDto;
import com.redartis.dto.transaction.TransactionMessageDto;
import com.redartis.expense.feign.RecognizerFeign;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Category;
import com.redartis.expense.model.Keyword;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.service.calc.TransactionHandler;
import com.redartis.expense.service.calc.TransactionHandlerImplInvalidTransaction;
import com.redartis.expense.service.calc.TransactionHandlerImplSingleAmountAtEnd;
import com.redartis.expense.service.calc.TransactionHandlerImplSingleAmountAtFront;
import com.redartis.expense.service.calc.TransactionHandlerImplSumAmountAtEnd;
import com.redartis.expense.service.calc.TransactionHandlerImplSumAmountAtFront;
import com.redartis.expense.util.TelegramUtils;
import jakarta.annotation.PostConstruct;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProcessingService {
    private static final ZoneOffset UKRAINE_OFFSET = ZoneOffset.ofHours(2);

    private final AccountService accountService;
    private final CategoryService categoryService;
    private final TelegramUtils telegramUtils;
    private final List<TransactionHandler> transactionHandlers;
    private final RecognizerFeign recognizerFeign;
    private final ExecutorService executorService;

    @PostConstruct
    public void init() {
        transactionHandlers.add(new TransactionHandlerImplSumAmountAtFront());
        transactionHandlers.add(new TransactionHandlerImplSingleAmountAtFront());
        transactionHandlers.add(new TransactionHandlerImplSumAmountAtEnd());
        transactionHandlers.add(new TransactionHandlerImplSingleAmountAtEnd());
        transactionHandlers.add(new TransactionHandlerImplInvalidTransaction());
    }

    public Transaction processTransaction(TransactionMessageDto transactionMessageDto) {
        Account account = accountService.getAccountByChatIdWithCategories(
                transactionMessageDto.getChatId()
        );
        String transactionMessage = transactionMessageDto.getMessage();
        TransactionAmountAndCommentDto transactionDetails = new TransactionAmountAndCommentDto();

        for (TransactionHandler t : transactionHandlers) {
            Pattern pattern = Pattern.compile(t.getRegExp());
            Matcher matcher = pattern.matcher(transactionMessage);
            if (matcher.find()) {
                transactionDetails.setAmount(t.calculateAmount(transactionMessage));
                transactionDetails.setComment(t.getTransactionComment(transactionMessage));
                break;
            }
        }

        return Transaction.builder()
                .account(account)
                .amount(transactionDetails.getAmount())
                .message(transactionDetails.getComment())
                .category(getTransactionCategory(transactionDetails.getComment(), account))
                .date(transactionMessageDto.getDate())
                .telegramUserId(transactionMessageDto.getUserId())
                .build();
    }

    public Transaction validateAndProcessTransaction(
            TransactionMessageDto transactionMessageDto,
            Principal principal) {
        LocalDateTime time = LocalDateTime.now(ZoneId.ofOffset("UTC", UKRAINE_OFFSET));

        if (principal != null) {
            Account account = accountService.getAccountByUserIdWithCategories(
                    telegramUtils.getTelegramId(principal)
            );
            transactionMessageDto.setChatId(account.getChatId());
            transactionMessageDto.setUserId(telegramUtils.getTelegramId(principal));
            transactionMessageDto.setDate(time);
        }

        return processTransaction(transactionMessageDto);
    }

    public void suggestCategoryToProcessedTransaction(
            TransactionMessageDto transactionMessageDto,
            UUID transactionId) {
        Transaction transaction = processTransaction(transactionMessageDto);
        List<CategoryDto> categories = categoryService.findCategoriesByUserId(
                transactionMessageDto.getUserId()
        );
        executorService.execute(() -> {
            if (!categories.isEmpty()) {
                recognizerFeign.recognizeCategory(
                        transaction.getMessage(),
                        transactionId,
                        categories
                );
            }
        });
    }

    private Category getTransactionCategory(String transactionMessage, Account account) {
        Set<Category> categories = account.getCategories();
        if (categories == null || categories.isEmpty()) {
            return null;
        }

        Category matchingCategory = getMatchingCategory(categories, transactionMessage);
        if (matchingCategory != null) {
            return matchingCategory;
        }

        Keyword matchingKeyword = getMatchingKeyword(categories, transactionMessage);
        return matchingKeyword != null ? matchingKeyword.getCategory() : null;
    }

    public Category getMatchingCategory(Set<Category> categories, String text) {
        return categories.stream()
                .filter(category -> category.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }

    private Keyword getMatchingKeyword(Set<Category> categories, String text) {
        return categories.stream()
                .map(Category::getKeywords)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(keyword -> keyword.getKeywordId().getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }
}
