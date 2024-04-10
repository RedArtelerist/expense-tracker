package com.redartis.expense.service;

import com.redartis.dto.transaction.TransactionDto;
import com.redartis.dto.transaction.TransactionMessageDto;
import com.redartis.dto.transaction.TransactionResponseDto;
import com.redartis.expense.exception.TransactionNotFoundException;
import com.redartis.expense.feign.TelegramBotFeign;
import com.redartis.expense.mapper.TransactionMapper;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Category;
import com.redartis.expense.model.Keyword;
import com.redartis.expense.model.KeywordId;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.model.User;
import com.redartis.expense.repository.CategoryRepository;
import com.redartis.expense.repository.KeywordRepository;
import com.redartis.expense.repository.TransactionRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionProcessingService transactionProcessingService;
    private final UserService userService;
    private final AccountService accountService;
    private final TelegramBotFeign telegramBotFeign;

    public long getTransactionsCount() {
        return transactionRepository.count();
    }

    public TransactionDto findTransactionById(UUID transactionId) {
        return transactionMapper.mapTransactionToDto(getTransactionById(transactionId));
    }

    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Can't find transaction with id: " + transactionId)
                );
    }

    public Transaction getTransactionByIdWithAccount(UUID transactionId) {
        return transactionRepository.findByIdWithAccount(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Can't find transaction with id: " + transactionId)
                );
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void saveAllTransactions(List<Transaction> transactionList) {
        transactionRepository.saveAll(transactionList);
    }

    @Transactional
    public void deleteTransactionById(UUID id) {
        Optional<Transaction> transactionToDelete = transactionRepository.findById(id);
        if (transactionToDelete.isPresent()) {
            getKeywordByTransaction(transactionToDelete.get())
                    .ifPresent(keywordRepository::delete);
            transactionRepository.deleteById(id);
            telegramBotFeign.deleteTelegramMessageById(id);
        }
    }

    @Transactional
    public void updateTransaction(TransactionDto transactionDto) {
        Transaction transaction = getTransactionByIdWithAccount(transactionDto.getId());
        transaction.setAmount(transactionDto.getAmount());

        Category category = categoryRepository.findCategoryByNameAndAccountId(
                transaction.getAccount().getId(),
                transactionDto.getCategoryName()
        );
        transaction.setCategory(category);

        Optional<Keyword> keyword = getKeywordByTransaction(transaction);
        if (keyword.isPresent() && !transaction.getMessage().equals(transactionDto.getMessage())) {
            keywordRepository.delete(keyword.get());
        }
        transaction.setMessage(transactionDto.getMessage());

        if (transaction.getCategory() != null
                && !transaction.getCategory().getName().equals(transactionDto.getCategoryName())) {
            keyword.ifPresent(keywordRepository::delete);
        } else if (!transactionDto.getCategoryName().equals("Unrecognized")
                   && transactionDto.getMessage() != null) {
            Keyword newKeyword = Keyword.builder()
                    .keywordId(new KeywordId(
                            transactionDto.getMessage(),
                            transaction.getAccount().getId())
                    )
                    .category(category)
                    .build();

            keywordRepository.save(newKeyword);
        }
        transactionRepository.save(transaction);
    }

    // TODO: optimize this method
    @Transactional
    public TransactionResponseDto updateTransactionFromTelegramChat(
            TransactionMessageDto transactionMessage,
            UUID id) {
        // Recognize the transaction to update
        var transaction = transactionProcessingService.processTransaction(transactionMessage);
        saveTransaction(transaction);
        transactionProcessingService.suggestCategoryToProcessedTransaction(
                transactionMessage,
                transaction.getId()
        );

        // Delete keyword if necessary
        transactionRepository.findById(id)
                .flatMap(this::getKeywordByTransaction)
                .filter(k -> !k.getKeywordId().getName().equalsIgnoreCase(transaction.getMessage()))
                .ifPresent(keywordRepository::delete);

        // Delete the old transaction
        transactionRepository.deleteById(id);

        return transactionMapper.mapTransactionToTelegramResponse(transaction);
    }

    private Optional<Keyword> getKeywordByTransaction(Transaction transaction) {
        KeywordId keywordId = new KeywordId();
        keywordId.setAccountId(transaction.getAccount().getId());
        keywordId.setName(transaction.getMessage());
        return keywordRepository.findByKeywordId(keywordId);
    }

    public Transaction enrichTransactionWithSuggestedCategory(TransactionDto transactionDto) {
        Transaction transaction = getTransactionById(transactionDto.getId());
        transaction.setSuggestedCategoryId(transactionDto.getSuggestedCategoryId());
        return transaction;
    }

    @Transactional
    public void setCategoryForAllUndefinedTransactionsWithSameKeywords(
            UUID transactionId,
            Long categoryId) {
        Long accountId = getTransactionByIdWithAccount(transactionId).getAccount().getId();
        String message = getTransactionById(transactionId).getMessage();
        transactionRepository.updateCategoryIdWhereCategoryIsNull(categoryId, message, accountId);
    }

    @Transactional
    public void removeCategoryFromTransactionsWithSameMessage(UUID transactionId) {
        Long accountId = getTransactionByIdWithAccount(transactionId).getAccount().getId();
        String message = getTransactionById(transactionId).getMessage();
        transactionRepository.removeCategoryIdFromTransactionsWithSameMessage(message, accountId);
    }

    public List<TransactionDto> findTransactionsByUserIdWithoutCategories(Long userId) {
        Account account = userService.getUserById(userId).getAccount();
        return transactionRepository.findAllWithoutCategoriesByAccountId(account.getId())
                .stream()
                .map(transactionMapper::mapTransactionToDto)
                .toList();
    }

    public List<TransactionDto> findTransactionsForAccountByChatId(Long chatId) {
        Account account = accountService.getAccountByChatId(chatId);
        return transactionRepository.findAllByAccountId(account.getId())
                .stream()
                .map(transactionMapper::mapTransactionToDto)
                .toList();
    }

    public List<TransactionDto> findTransactionsByUserIdLimited(
            Long id,
            Integer pageSize,
            Integer pageNumber) {
        Account account = userService.getUserById(id).getAccount();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("date").descending());

        var transactionList = transactionRepository.findAllByAccountId(account.getId(), pageable)
                .getContent()
                .stream()
                .map(transactionMapper::mapTransactionToDto)
                .toList();
        return enrichTransactionsWithTgUsernames(transactionList);
    }

    private List<TransactionDto> enrichTransactionsWithTgUsernames(
            List<TransactionDto> transactionList) {
        Map<Long, User> userMap = userService.getUsersByIds(transactionList.stream()
                        .map(TransactionDto::getTelegramUserId)
                        .distinct()
                        .toList())
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        transactionList.forEach(transactionDto -> {
            User user = userMap.get(transactionDto.getTelegramUserId());
            if (user != null) {
                if (user.getUsername() != null) {
                    transactionDto.setTelegramUserName(user.getUsername());
                } else {
                    transactionDto.setTelegramUserName(user.getFirstName());
                }
            }
        });

        return transactionList;
    }

    public List<TransactionDto> getTransactionsByPeriodAndCategory(
            Integer year,
            Integer month,
            String categoryName) {
        return transactionRepository.findTransactionsBetweenDatesAndCategory(
                        year,
                        month,
                        categoryName)
                .stream()
                .map(transactionMapper::mapTransactionToDto)
                .collect(Collectors.toList());
    }
}
