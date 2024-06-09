package com.redartis.auth.controller;

import com.redartis.auth.model.AuthResponse;
import com.redartis.auth.service.AuthService;
import com.redartis.dto.auth.TelegramAuthRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authenticationService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody TelegramAuthRequest authRequest)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return authenticationService.login(authRequest);
    }
}
