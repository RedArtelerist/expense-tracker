package com.redartis.expense.service;

import com.redartis.dto.auth.TelegramAuthRequest;
import com.redartis.expense.exception.TelegramAuthException;
import com.redartis.expense.security.JwtProvider;
import com.redartis.expense.security.dto.JwtResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final TelegramVerificationService telegramVerificationService;
    private final JwtProvider jwtProvider;

    public JwtResponse login(TelegramAuthRequest telegramAuthRequest)
            throws NoSuchAlgorithmException, InvalidKeyException {
        if (telegramVerificationService.verify(telegramAuthRequest)) {
            var user = userService.saveUser(telegramAuthRequest);
            return new JwtResponse(
                    jwtProvider.generateAccessToken(user),
                    jwtProvider.generateRefreshToken(user)
            );
        } else {
            throw new TelegramAuthException(String.format("""
                            Telegram authentication failed for user %s: \
                            encoded data does not match with the hash""",
                    telegramAuthRequest.username()
            ));
        }
    }
}
