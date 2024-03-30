package com.redartis.expense.config.filter;

import com.redartis.expense.annotations.OnlyServiceUse;
import com.redartis.expense.exception.InternalKeyNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestHeaderInterceptor implements HandlerInterceptor {
    private static final String HEADER_NAME = "X-INTERNAL-KEY";

    @Value("${filters.authorization-header.header-value}")
    private String secretKey;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        if (handler instanceof HandlerMethod) {
            OnlyServiceUse onlyServiceUse = ((HandlerMethod) handler)
                    .getMethodAnnotation(OnlyServiceUse.class);

            if (onlyServiceUse != null) {
                String internalKeyRequest = request.getHeader(HEADER_NAME);

                if (internalKeyRequest == null || !internalKeyRequest.equals(secretKey)) {
                    throw new InternalKeyNotFoundException("Доступ к странице запрещен");
                }
            }
        }
        return true;
    }
}
