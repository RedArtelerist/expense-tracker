package com.redartis.expense.service;

import com.redartis.expense.exception.TelegramAuthException;
import com.redartis.expense.model.User;
import com.redartis.expense.security.JwtProvider;
import com.redartis.expense.security.dto.JwtAuthentication;
import com.redartis.expense.security.dto.JwtResponse;
import com.redartis.expense.security.dto.TelegramAuthRequest;
import io.jsonwebtoken.Claims;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.management.InstanceNotFoundException;
import javax.naming.AuthenticationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final TelegramVerificationService telegramVerificationService;
    private final JwtProvider jwtProvider;

    public JwtResponse login(TelegramAuthRequest telegramAuthRequest)
            throws NoSuchAlgorithmException, InvalidKeyException, InstanceNotFoundException {
        if (telegramVerificationService.verify(telegramAuthRequest)) {
            userService.saveUser(telegramAuthRequest);
            User user = userService.getUserById(telegramAuthRequest.getId());
            return new JwtResponse(
                    jwtProvider.generateAccessToken(user),
                    jwtProvider.generateRefreshToken(user)
            );
        } else {
            throw new TelegramAuthException("Telegram authentication failed for user "
                    + telegramAuthRequest.getUsername()
                    + ": encoded data does not match with the hash");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken)
            throws AuthenticationException, InstanceNotFoundException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String id = claims.getSubject();
            final User user = userService.getUserById(Long.parseLong(id));
            if (Objects.isNull(user)) {
                throw new AuthenticationException("No user registered with this token");
            }
            final String accessToken = jwtProvider.generateAccessToken(user);
            return new JwtResponse(accessToken, null);
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken)
            throws AuthenticationException, InstanceNotFoundException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String id = claims.getSubject();
            final User user = userService.getUserById(Long.parseLong(id));
            if (Objects.isNull(user)) {
                throw new AuthenticationException("No user registered with this token");
            }
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String newRefreshToken = jwtProvider.generateRefreshToken(user);
            return new JwtResponse(accessToken, newRefreshToken);
        }
        throw new AuthenticationException("Invalid JWT token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
