package com.redartis.tg.constants;

import lombok.Getter;

@Getter
public enum InlineKeyboardCallback {
    DEFAULT("Ні", "default"),
    MERGE_CATEGORIES("Перенести тіки категорії", "mergeWithCategories"),
    MERGE_CATEGORIES_AND_TRANSACTIONS(
            "Перенести категорії і транзакції",
            "mergeWithCategoriesAndTransactions"
    );

    private final String text;
    private final String data;

    InlineKeyboardCallback(String text, String data) {
        this.text = text;
        this.data = data;
    }
}
