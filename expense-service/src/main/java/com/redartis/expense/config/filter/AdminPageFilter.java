package com.redartis.expense.config.filter;

import static com.redartis.expense.security.JwtUtils.getTokenFromRequest;

import com.redartis.expense.security.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminPageFilter extends OncePerRequestFilter {
    private static final String ADMIN_PATH = "/admin";
    private static final String USERNAME_CLAIM = "username";

    private final JwtService jwtService;

    @Value("${admin.allowed_users}")
    private String[] allowedUsers;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        Claims claims = jwtService.getClaims(token);
        String username = claims.get(USERNAME_CLAIM, String.class);

        for (String allowedUsername : allowedUsers) {
            if (username.equals(allowedUsername)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith(ADMIN_PATH);
    }
}
