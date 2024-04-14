package com.redartis.expense.controller.rest;

import com.redartis.expense.security.dto.JwtResponse;
import com.redartis.expense.security.dto.TelegramAuthRequest;
import com.redartis.expense.service.AuthenticationService;
import com.redartis.expense.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
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
    private final AuthenticationService authenticationService;
    private final CookieUtils cookieUtils;

    @PostMapping("/login")
    public void login(@RequestBody TelegramAuthRequest authRequest, HttpServletResponse response)
            throws NoSuchAlgorithmException, InvalidKeyException {
        JwtResponse token = authenticationService.login(authRequest);
        cookieUtils.addAccessTokenCookie(token, response);
    }
}
