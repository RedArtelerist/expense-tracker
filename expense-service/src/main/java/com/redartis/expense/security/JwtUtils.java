package com.redartis.expense.security;

import com.redartis.expense.security.dto.JwtAuthentication;
import io.jsonwebtoken.Claims;

public class JwtUtils {
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setTelegramId(Long.parseLong(claims.getSubject()));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.get("username", String.class));
        return jwtInfoToken;
    }
}
