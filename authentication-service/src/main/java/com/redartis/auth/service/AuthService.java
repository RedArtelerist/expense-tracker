package com.redartis.auth.service;

import com.redartis.auth.dto.AuthResponse;
import com.redartis.auth.exception.TelegramAuthException;
import com.redartis.auth.feign.UserService;
import com.redartis.dto.auth.TelegramAuthRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final TelegramVerificationService telegramVerificationService;

    public AuthResponse login(TelegramAuthRequest authRequest)
            throws NoSuchAlgorithmException, InvalidKeyException {
        if (telegramVerificationService.verify(authRequest)) {
            var user = userService.saveTelegramUserData(authRequest);

            return new AuthResponse(
                    jwtService.generateAccessToken(user),
                    jwtService.generateRefreshToken(user)
            );
        }
        throw new TelegramAuthException(String.format("""
                        Telegram authentication failed for user %s: \
                        encoded data does not match with the hash""",
                authRequest.username()
        ));
    }
}
