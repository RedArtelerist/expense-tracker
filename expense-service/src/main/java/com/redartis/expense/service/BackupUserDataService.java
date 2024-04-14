package com.redartis.expense.service;

import com.redartis.dto.account.BackupUserDataDto;
import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.transaction.TransactionDto;
import com.redartis.expense.mapper.CategoryMapper;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Category;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackupUserDataService {
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final KeywordService keywordService;
    private final TransactionService transactionService;
    private final TransactionProcessingService transactionProcessingService;
    private final CategoryMapper categoryMapper;

    public BackupUserDataDto createBackupUserData(Long telegramId) {
        Long chatId = accountService.getAccountByUserId(telegramId).getChatId();
        return createBackup(chatId);
    }

    public BackupUserDataDto createBackupRemovedUserData(Long chatId, Long userId) {
        return createBackupForGroupMember(chatId, userId);
    }

    public BackupUserDataDto createBackup(Long chatId) {
        var categories = categoryService.findCategoriesByChatId(chatId);
        var transactions = transactionService.findTransactionsForAccountByChatId(chatId);

        return BackupUserDataDto.builder()
                .categories(categories)
                .transactions(transactions)
                .build();
    }

    public BackupUserDataDto createBackupForGroupMember(Long chatId, Long userId) {
        var categories = categoryService.findCategoriesByChatId(chatId);
        var transactions = transactionService
                .findTransactionsForAccountByChatIdAndUserId(chatId, userId);

        return BackupUserDataDto.builder()
                .categories(categories)
                .transactions(transactions)
                .build();
    }

    private BackupUserDataDto createBackupForGroupMember(
            Long chatId,
            Long userId,
            List<CategoryDto> categories) {
        var transactions = transactionService
                .findTransactionsForAccountByChatIdAndUserId(chatId, userId);

        return BackupUserDataDto.builder()
                .categories(categories)
                .transactions(transactions)
                .build();
    }

    public Map<Long, BackupUserDataDto> createBackupForGroupAccount(Long chatId) {
        Account account = accountService.getAccountByChatIdWithUsers(chatId);
        var categories = categoryService.findCategoriesByChatId(chatId);
        return account.getUsers().stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> createBackupForGroupMember(chatId, user.getId(), categories)
                ));
    }

    public void writingDataFromBackupFile(BackupUserDataDto backupUserDataDto, Long telegramId) {
        var transactions = createTransactionsFromBackupFile(backupUserDataDto, telegramId);
        transactionService.saveAllTransactions(transactions);
    }

    public List<Transaction> createTransactionsFromBackupFile(BackupUserDataDto backupUserData,
                                                              Long telegramId) {
        List<TransactionDto> transactions = backupUserData.transactions();
        List<CategoryDto> categories = backupUserData.categories();

        // Delete all transactions and categories for account
        Account account = accountService.getNewAccount(telegramId);
        Long accountId = account.getId();
        accountService.deletingAllTransactionsCategoriesKeywordsByAccountId(accountId);

        transactions.stream()
                .filter(transaction -> transaction.getCategoryName() == null)
                .forEach(transaction -> transaction.setCategoryName("Unrecognized"));

        // Save categories from backup file for account
        var categorySet = categories.stream()
                .map(categoryDto -> categoryMapper.mapToCategory(categoryDto, account))
                .collect(Collectors.toSet());
        categoryService.saveAllCategories(categorySet);

        // Return transactions from backup file
        return transactions.stream()
                .map(transactionDto -> Transaction.builder()
                        .amount(transactionDto.getAmount())
                        .message(transactionDto.getMessage())
                        .category(getCategory(transactionDto, categories, categorySet, accountId))
                        .account(account)
                        .date(transactionDto.getDate())
                        .telegramUserId(transactionDto.getTelegramUserId())
                        .build())
                .toList();
    }

    private Category getCategory(TransactionDto transactionDto,
                                 List<CategoryDto> categoryDtoList,
                                 Set<Category> categories,
                                 Long accountId) {
        String categoryName = transactionDto.getCategoryName();
        Optional<CategoryDto> categoryDto = categoryDtoList.stream()
                .filter(category -> categoryName.equals(category.name()))
                .findFirst();

        var category = transactionProcessingService.getMatchingCategory(categories, categoryName);
        categoryDto.ifPresent(
                it -> keywordService.setKeywordsFromCategoryDto(it, category, accountId)
        );
        return category;
    }
}
