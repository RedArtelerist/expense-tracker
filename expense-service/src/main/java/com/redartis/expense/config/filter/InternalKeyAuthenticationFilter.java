package com.redartis.expense.config.filter;

import com.redartis.expense.security.dto.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class InternalKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final String HEADER_NAME = "X-INTERNAL-KEY";

    @Value("${filters.authorization-header.header-value}")
    private String secretKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String securityHeader = request.getHeader(HEADER_NAME);
        return securityHeader == null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String internalKey = request.getHeader(HEADER_NAME);
        if ((internalKey != null) && internalKey.equals(secretKey)) {
            Authentication authentication = new JwtAuthentication();
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
