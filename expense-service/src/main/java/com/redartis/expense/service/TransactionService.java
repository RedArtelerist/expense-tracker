package com.redartis.expense.service;

import com.redartis.dto.transaction.TransactionDto;
import com.redartis.expense.exception.TransactionNotFoundException;
import com.redartis.expense.mapper.TransactionMapper;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Transaction;
import com.redartis.expense.model.User;
import com.redartis.expense.repository.TransactionRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    public long getTransactionsCount() {
        return transactionRepository.count();
    }

    public List<TransactionDto> findTransactionsByUserIdWithoutCategories(Long id) {
        Account account = userService.getUserById(id).getAccount();
        return transactionRepository.findAllWithoutCategoriesByAccountId(account.getId())
                .stream()
                .map(transactionMapper::mapTransactionToDto)
                .toList();
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
                        .toList()
                ).stream()
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
            long categoryId) {
        return transactionRepository.findTransactionsBetweenDatesAndCategory(
                        year,
                        month,
                        categoryId
                ).stream()
                .map(transactionMapper::mapTransactionToDto)
                .collect(Collectors.toList());
    }
}
