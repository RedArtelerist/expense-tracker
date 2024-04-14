package com.redartis.expense.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TelegramAuthRequest(
        @JsonProperty("id")
        Long id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("username")
        String username,
        @JsonProperty("photo_url")
        String photoUrl,
        @JsonProperty("auth_date")
        String authDate,
        @JsonProperty("hash")
        String hash
) {}
