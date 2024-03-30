package com.redartis.expense.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TelegramAuthRequest {
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String username;

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("auth_date")
    private String authDate;

    private String hash;
}
