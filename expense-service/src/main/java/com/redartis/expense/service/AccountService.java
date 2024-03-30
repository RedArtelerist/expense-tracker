package com.redartis.expense.service;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.expense.exception.AccountNotFoundException;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.User;
import com.redartis.expense.repository.AccountRepository;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final TelegramUtils telegramUtils;

    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id " + id + " does not exist")
        );
    }

    public Account getAccountByChatId(Long chatId) {
        return accountRepository.findByChatId(chatId).orElseThrow(
                () -> new AccountNotFoundException("Can't find account by chatId=" + chatId));
    }

    public Account getAccountByUserId(Long id) {
        return userService.getUserByIdWithAccountCategories(id).getAccount();
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
    }

    public void registerSingleAccount(AccountDataDto accountDataDto) {
        User user = userService.getUserById(accountDataDto.getUserId());
        Account account = new Account(accountDataDto.getChatId());
        account.addUser(user);
        accountRepository.save(account);
        userService.saveUser(user);
    }

    public void registerSingleAccount(Principal principal) {
        Long telegramId = telegramUtils.getTelegramId(principal);
        User user = userService.getUserById(telegramId);
        if (accountRepository.findByChatId(user.getId()).isPresent()) {
            return;
        }
        Account account = new Account(telegramId);
        account.addUser(user);

        accountRepository.save(account);
        userService.saveUser(user);
    }
}
