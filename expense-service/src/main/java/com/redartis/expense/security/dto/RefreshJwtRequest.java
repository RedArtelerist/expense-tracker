package com.redartis.expense.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshJwtRequest {
    private String refreshToken;
}
