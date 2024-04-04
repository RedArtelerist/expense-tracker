package com.redartis.expense.service;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.dto.telegram.ChatMemberDto;
import com.redartis.expense.exception.AccountNotFoundException;
import com.redartis.expense.mapper.UserMapper;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.User;
import com.redartis.expense.repository.AccountRepository;
import com.redartis.expense.repository.CategoryRepository;
import com.redartis.expense.repository.TransactionRepository;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final TelegramUtils telegramUtils;
    private final UserMapper userMapper;

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id " + id + " does not exist")
        );
    }

    public Account getAccountByChatId(Long chatId) {
        return accountRepository.findByChatId(chatId).orElseThrow(
                () -> new AccountNotFoundException("Can't find account by chatId=" + chatId));
    }

    public Account getAccountByChatIdWithCategories(Long chatId) {
        return accountRepository.findByChatIdWithCategories(chatId).orElseThrow(
                () -> new AccountNotFoundException("Can't find account by chatId=" + chatId));
    }

    public Account getAccountByUserId(Long userId) {
        return userService.getUserById(userId).getAccount();
    }

    public Account getAccountByUserIdWithCategories(Long userId) {
        return userService.getUserByIdWithAccountCategories(userId).getAccount();
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
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

    public void registerSingleAccount(AccountDataDto accountDataDto) {
        User user = userService.getUserById(accountDataDto.getUserId());
        Account account = accountRepository.findByChatId(accountDataDto.getChatId())
                .orElse(null);

        if (account != null) {
            user.setAccount(account);
            userService.saveUser(user);
            return;
        }
        account = new Account(accountDataDto.getChatId());
        account.addUser(user);

        accountRepository.save(account);
        userService.saveUser(user);
    }

    public void registerGroupAccount(AccountDataDto accountDataDto) {
        User user = userService.getUserById(accountDataDto.getUserId());
        Account account = new Account(accountDataDto.getChatId());
        account.addUser(user);

        accountRepository.save(account);
        userService.saveUser(user);
    }

    public void addNewChatMemberToAccount(ChatMemberDto chatMemberDto) {
        Account account = getAccountByChatId(chatMemberDto.chatId());
        User user = userMapper.mapChatMemberDtoToUser(chatMemberDto);
        user.setAccount(account);
        userService.saveUser(user);
    }

    @Transactional
    public void addNewChatMembersToAccount(List<ChatMemberDto> chatMembers) {
        chatMembers.forEach(this::addNewChatMemberToAccount);
    }

    public void removeChatMemberFromAccount(ChatMemberDto chatMemberDto) {
        User user = userService.getUserById(chatMemberDto.userId());
        Account account = getAccountByChatId(chatMemberDto.userId());

        if (account != null) {
            user.setAccount(account);
            userService.saveUser(user);
        } else {
            user.setAccount(null);
            userService.saveUser(user);
        }
    }

    @Transactional
    public void mergeToGroupAccountWithCategoriesAndWithoutTransactions(Long userId) {
        Account oldAccount = getOldAccount(userId);
        Account newAccount = getNewAccount(userId);

        updateAccountCategories(oldAccount, newAccount);
    }

    @Transactional
    public void mergeToGroupAccountWithCategoriesAndTransactions(Long userId) {
        Account oldAccount = getOldAccount(userId);
        Account newAccount = getNewAccount(userId);

        updateAccountCategories(oldAccount, newAccount);
        updateAccountTransactions(oldAccount, newAccount);
    }

    public void updateAccountCategories(Account oldAccount, Account newAccount) {
        categoryRepository.updateAccountId(oldAccount.getId(), newAccount.getId());
    }

    public void updateAccountTransactions(Account oldAccount, Account newAccount) {
        transactionRepository.updateAccountId(oldAccount.getId(), newAccount.getId());
    }

    private Account getOldAccount(Long userId) {
        return getAccountByChatId(userId);
    }

    private Account getNewAccount(Long userId) {
        return accountRepository.findNewAccountByUserId(userId);
    }

    public int getGroupAccountsCount() {
        return accountRepository.getGroupAccountsCount();
    }
}