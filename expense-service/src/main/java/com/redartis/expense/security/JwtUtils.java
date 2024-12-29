package com.redartis.expense.security;

import com.redartis.expense.security.dto.JwtAuthentication;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;

public class JwtUtils {
    private static final String TOKEN_SEPARATOR = "Bearer ";

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setTelegramId(Long.parseLong(claims.getSubject()));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.get("username", String.class));
        return jwtInfoToken;
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_SEPARATOR)) {
            return bearerToken.substring(TOKEN_SEPARATOR.length());
        }
        return null;
    }

    public static String getTokenFromRequest(StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_SEPARATOR)) {
            return bearerToken.substring(TOKEN_SEPARATOR.length());
        }
        return null;
    }
}
