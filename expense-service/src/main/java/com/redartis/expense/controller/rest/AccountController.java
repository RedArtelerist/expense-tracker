package com.redartis.expense.controller.rest;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.expense.service.AccountService;
import com.redartis.expense.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
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
}
