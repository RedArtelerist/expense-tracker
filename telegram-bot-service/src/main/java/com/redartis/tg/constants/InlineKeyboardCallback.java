package com.redartis.tg.constants;

import lombok.Getter;

@Getter
public enum InlineKeyboardCallback {
    DEFAULT("No", "default"),
    MERGE_CATEGORIES("Transfer categories", "mergeWithCategories"),
    MERGE_CATEGORIES_AND_TRANSACTIONS(
            "Transfer categories and transactions",
            "mergeWithCategoriesAndTransactions"
    );

    private final String text;
    private final String data;

    InlineKeyboardCallback(String text, String data) {
        this.text = text;
        this.data = data;
    }
}
