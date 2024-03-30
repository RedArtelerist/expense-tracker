package com.redartis.expense.config.filter;

import com.redartis.expense.security.JwtProvider;
import com.redartis.expense.security.JwtUtils;
import com.redartis.expense.security.dto.JwtAuthentication;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private static final String ACCESS_TOKEN = "accessToken";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            Claims claims = jwtProvider.getAccessClaims(token);
            JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        filterChain.doFilter(request, response);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (!Objects.isNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN.equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }
}

