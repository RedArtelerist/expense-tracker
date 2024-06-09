package com.redartis.expense.security;

import com.redartis.dto.auth.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
public class JwtProvider {
    @Value("${jwt.token.lifetime-in-hours}")
    private int tokenLifeTimeInHours;
    @Value("${jwt.token.lifetime-in-days}")
    private int tokenLifeTimeInDays;

    private final SecretKey jwtSecret;

    public JwtProvider(@Value("${jwt.secret.access}") String jwtSecret) {
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
                .claim("firstName", user.firstName())
                .claim("username", user.username())
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
                .signWith(jwtSecret)
                .compact();
    }

    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Token expired", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported jwt", ex);
        } catch (MalformedJwtException ex) {
            log.error("Malformed jwt", ex);
        } catch (SignatureException ex) {
            log.error("Invalid signature", ex);
        } catch (Exception ex) {
            log.error("invalid token", ex);
        }
        return false;
    }

    public Claims getClaims(@NonNull String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
