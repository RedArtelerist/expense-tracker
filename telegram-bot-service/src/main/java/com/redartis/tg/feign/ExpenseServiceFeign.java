package com.redartis.tg.feign;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.dto.account.BackupUserDataDto;
import com.redartis.dto.telegram.ChatMemberDto;
import com.redartis.dto.transaction.TransactionMessageDto;
import com.redartis.dto.transaction.TransactionResponseDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "expense-service")
public interface ExpenseServiceFeign {
    @PostMapping("/transactions")
    TransactionResponseDto sendTransaction(@RequestBody TransactionMessageDto transaction);

    @PostMapping("/accounts/register/single")
    void registerSingleAccount(@RequestBody AccountDataDto accountData);

    @PostMapping("/accounts/register/group")
    void registerGroupAccount(@RequestBody AccountDataDto accountData);

    @PostMapping("/accounts/merge/categories")
    void mergeAccountWithCategoriesWithoutTransactions(@RequestParam Long userId);

    @PostMapping("/accounts/merge/transactions")
    void mergeAccountWithCategoriesAndTransactions(@RequestParam Long userId);

    @PostMapping("/accounts/add/user")
    void addNewChatMemberToAccount(@RequestBody ChatMemberDto chatMember);

    @PostMapping("/accounts/add/users")
    void addNewChatMembersToAccount(@RequestBody List<ChatMemberDto> newChatMembers);

    @PostMapping("/accounts/remove/user")
    void removeChatMemberFromAccount(@RequestBody ChatMemberDto chatMember);

    @DeleteMapping("/accounts/{chatId}")
    void deleteGroupAccount(@PathVariable Long chatId);

    @PutMapping("/transactions/update/{id}")
    TransactionResponseDto submitTransactionForUpdate(
            @RequestBody TransactionMessageDto transactionMessage,
            @PathVariable("id") UUID id);

    @DeleteMapping("/transactions/{id}")
    void deleteTransactionById(@PathVariable("id") UUID id);

    @GetMapping("/settings/backup/{chatId}/{userId}")
    BackupUserDataDto getBackupForChatMember(@PathVariable Long chatId, @PathVariable Long userId);

    @GetMapping("/settings/backup/{chatId}")
    Map<Long, BackupUserDataDto> getBackupForGroupAccount(@PathVariable Long chatId);
}
