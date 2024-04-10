package com.redartis.expense.controller.rest;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.dto.telegram.ChatMemberDto;
import com.redartis.expense.service.AccountService;
import com.redartis.expense.service.UserService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    @GetMapping("/register/single")
    public void registerSingleAccount(Principal principal) {
        accountService.registerSingleAccount(principal);
    }

    @PostMapping("/register/single")
    public void registerSingleAccount(@RequestBody AccountDataDto accountDataDto) {
        userService.saveUser(accountDataDto);
        accountService.registerSingleAccount(accountDataDto);
    }

    @PostMapping("/register/group")
    public void registerGroupAccount(@RequestBody AccountDataDto accountDataDto) {
        accountService.registerGroupAccount(accountDataDto);
    }

    @PostMapping("/add/users")
    public void addNewChatMembersToAccount(@RequestBody List<ChatMemberDto> chatMembers) {
        accountService.addNewChatMembersToAccount(chatMembers);
    }

    @PostMapping("/add/user")
    public void addNewChatMemberToAccount(@RequestBody ChatMemberDto chatMemberDto) {
        accountService.addNewChatMemberToAccount(chatMemberDto);
    }

    @PostMapping("/remove/user")
    public void removeChatMemberFromAccount(@RequestBody ChatMemberDto chatMemberDto) {
        accountService.removeChatMemberFromAccount(chatMemberDto);
    }

    @PostMapping("/merge/categories")
    public void mergeCategories(@RequestParam Long userId) {
        accountService.mergeToGroupAccountWithCategoriesAndWithoutTransactions(userId);
    }

    @PostMapping("/merge/transactions")
    public void mergeTransactions(@RequestParam Long userId) {
        accountService.mergeToGroupAccountWithCategoriesAndTransactions(userId);
    }

    @GetMapping("/count")
    public int getAccountsCount() {
        return accountService.getGroupAccountsCount();
    }
}
