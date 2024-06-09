package com.redartis.apigateway.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {
    private final SecretKey jwtAccessSecret;

    public JwtUtil(@Value("${jwt.secret.access}") String jwtAccessSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtAccessSecret)
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
}
