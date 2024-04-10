package com.redartis.dto.constants;

import lombok.Getter;

@Getter
public enum StatusMailing {
    SUCCESS("Successfully"),
    ERROR("Error"),
    PENDING("Pending");

    private final String value;

    StatusMailing(String value) {
        this.value = value;
    }
}
