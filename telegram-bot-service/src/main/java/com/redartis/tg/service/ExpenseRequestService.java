package com.redartis.tg.service;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.dto.account.BackupUserDataDto;
import com.redartis.dto.telegram.ChatMemberDto;
import com.redartis.dto.transaction.TransactionMessageDto;
import com.redartis.dto.transaction.TransactionResponseDto;
import com.redartis.tg.feign.ExpenseServiceFeign;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseRequestService {
    private final ExpenseServiceFeign expenseServiceFeign;

    public TransactionResponseDto sendTransaction(TransactionMessageDto transaction) {
        return expenseServiceFeign.sendTransaction(transaction);
    }

    public void registerSingleAccount(AccountDataDto accountData) {
        expenseServiceFeign.registerSingleAccount(accountData);
    }

    public void registerGroupAccount(AccountDataDto accountDataDto) {
        expenseServiceFeign.registerGroupAccount(accountDataDto);
    }

    public void registerGroupAccountAndMergeCategoriesWithoutTransactions(
            AccountDataDto accountDataDto) {
        registerGroupAccount(accountDataDto);
        mergeWithCategoriesAndWithoutTransactions(accountDataDto.getUserId());
    }

    public void registerGroupAccountAndMergeWithCategoriesAndTransactions(
            AccountDataDto accountDataDto) {
        registerGroupAccount(accountDataDto);
        mergeWithCategoryAndTransactions(accountDataDto.getUserId());
    }

    public void mergeWithCategoriesAndWithoutTransactions(Long userId) {
        expenseServiceFeign.mergeAccountWithCategoriesWithoutTransactions(userId);
    }

    public void mergeWithCategoryAndTransactions(Long userId) {
        expenseServiceFeign.mergeAccountWithCategoriesAndTransactions(userId);
    }

    public void addNewChatMembersToAccount(List<ChatMemberDto> newChatMembers) {
        expenseServiceFeign.addNewChatMembersToAccount(newChatMembers);
    }

    public void addNewChatMemberToAccount(ChatMemberDto newChatMember) {
        expenseServiceFeign.addNewChatMemberToAccount(newChatMember);
    }

    public void removeChatMemberFromAccount(ChatMemberDto leftChatMember) {
        expenseServiceFeign.removeChatMemberFromAccount(leftChatMember);
    }

    public void deleteGroupAccount(Long chatId) {
        expenseServiceFeign.deleteGroupAccount(chatId);
    }

    public TransactionResponseDto submitTransactionForUpdate(
            TransactionMessageDto transaction,
            UUID id) {
        return expenseServiceFeign.submitTransactionForUpdate(transaction, id);
    }

    public void deleteTransactionById(UUID id) {
        expenseServiceFeign.deleteTransactionById(id);
    }

    public BackupUserDataDto getBackup(Long chatId, Long userId) {
        return expenseServiceFeign.getBackupForChatMember(chatId, userId);
    }

    public Map<Long, BackupUserDataDto> getBackupsForGroupAccount(Long chatId) {
        return expenseServiceFeign.getBackupForGroupAccount(chatId);
    }
}
