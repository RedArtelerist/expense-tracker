package com.redartis.apigateway.config;

import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    private static final String TOKEN_SEPARATOR = "Bearer ";
    private static final List<String> openApiEndpoints = List.of(
            "/auth/login"
    );

    private final JwtUtil jwtUtil;
    private final Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    @Autowired
    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return onError(exchange);
            }

            String token = getAuthHeader(request);
            if (token != null && !jwtUtil.validateToken(token)) {
                log.error("Invalid token");
                return onError(exchange);
            }
        }

        log.info("Authentication passed");

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private String getAuthHeader(ServerHttpRequest request) {
        String bearerToken = request.getHeaders()
                .getOrEmpty(HttpHeaders.AUTHORIZATION)
                .getFirst();

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_SEPARATOR)) {
            return bearerToken.substring(TOKEN_SEPARATOR.length());
        }
        return null;
    }
}
