package com.redartis.expense.security;

import com.redartis.expense.model.User;
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

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtProvider(@Value("${jwt.secret.access}") String jwtAccessSecret,
                       @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(@NonNull User user) {
        Instant accessExpirationInstant = LocalDateTime.now()
                .plusHours(tokenLifeTimeInHours)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Jwts.builder()
                .subject(user.getId().toString())
                .expiration(Date.from(accessExpirationInstant))
                .claim("firstName", user.getFirstName())
                .claim("username", user.getUsername())
                .signWith(jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(@NonNull User user) {
        final Instant refreshExpirationInstant = LocalDateTime.now()
                .plusDays(tokenLifeTimeInDays)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Jwts.builder()
                .subject(user.getId().toString())
                .expiration(Date.from(refreshExpirationInstant))
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
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

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
