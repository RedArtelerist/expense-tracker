package com.redartis.auth.service;

import com.redartis.dto.auth.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtService {
    @Value("${jwt.token.lifetime-in-hours}")
    private int tokenLifeTimeInHours;
    @Value("${jwt.token.lifetime-in-days}")
    private int tokenLifeTimeInDays;

    private final SecretKey jwtSecret;

    public JwtService(@Value("${jwt.secret.access}") String jwtSecret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateAccessToken(@NonNull UserDto user) {
        Instant accessExpirationInstant = LocalDateTime.now()
                .plusHours(tokenLifeTimeInHours)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Jwts.builder()
                .subject(user.id().toString())
                .expiration(Date.from(accessExpirationInstant))
                .claim("username", user.username())
                .claim("firstName", user.firstName())
                .signWith(jwtSecret)
                .compact();
    }

    public String generateRefreshToken(@NonNull UserDto user) {
        final Instant refreshExpirationInstant = LocalDateTime.now()
                .plusDays(tokenLifeTimeInDays)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Jwts.builder()
                .subject(user.id().toString())
                .expiration(Date.from(refreshExpirationInstant))
                .claim("username", user.username())
                .claim("firstName", user.firstName())
                .signWith(jwtSecret)
                .compact();
    }
}
