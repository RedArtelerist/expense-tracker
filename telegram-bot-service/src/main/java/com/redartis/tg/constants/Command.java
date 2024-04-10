package com.redartis.tg.constants;

import lombok.Getter;

@Getter
public enum Command {
    START("/start", "This bot is designed to track your finances\n");

    private final String alias;
    private final String description;

    Command(String alias, String description) {
        this.alias = alias;
        this.description = description;
    }
}
