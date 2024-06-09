package com.redartis.expense.controller.rest;

import com.redartis.dto.auth.TelegramAuthRequest;
import com.redartis.dto.auth.UserDto;
import com.redartis.expense.annotations.OnlyServiceUse;
import com.redartis.expense.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @OnlyServiceUse
    @PostMapping("/save")
    public UserDto saveTelegramUserData(@RequestBody TelegramAuthRequest authRequest) {
        return userService.saveUser(authRequest);
    }
}
