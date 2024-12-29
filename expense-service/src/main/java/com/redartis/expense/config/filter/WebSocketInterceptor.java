package com.redartis.expense.config.filter;

import static com.redartis.expense.security.JwtUtils.getTokenFromRequest;

import com.redartis.expense.security.JwtService;
import com.redartis.expense.security.JwtUtils;
import com.redartis.expense.security.dto.JwtAuthentication;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;

    @SneakyThrows
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
            String authToken = getTokenFromRequest(accessor);

            if (authToken == null || !jwtService.validateToken(authToken)) {
                throw new AuthException("Authentication failed!!");
            }

            Claims claims = jwtService.getClaims(authToken);
            JwtAuthentication jwtAuthentication = JwtUtils.generate(claims);
            jwtAuthentication.setAuthenticated(true);

            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            accessor.setUser(jwtAuthentication);
        }
        return message;
    }
}
