package com.redartis.tg.util;

import com.redartis.tg.constants.InlineKeyboardCallback;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class InlineKeyboardMarkupUtil {
    public InlineKeyboardMarkup generateMergeRequestMarkup() {
        var defaultInlineKeyboard = InlineKeyboardButton.builder()
                .text(InlineKeyboardCallback.DEFAULT.getText())
                .callbackData(InlineKeyboardCallback.DEFAULT.getData())
                .build();

        var mergeWithCategoriesKeyboard = InlineKeyboardButton.builder()
                .text(InlineKeyboardCallback.MERGE_CATEGORIES.getText())
                .callbackData(InlineKeyboardCallback.MERGE_CATEGORIES.getData())
                .build();

        var mergeWithCategoriesAndTransactionsKeyboard = InlineKeyboardButton.builder()
                .text(InlineKeyboardCallback.MERGE_CATEGORIES_AND_TRANSACTIONS.getText())
                .callbackData(InlineKeyboardCallback.MERGE_CATEGORIES_AND_TRANSACTIONS.getData())
                .build();

        var rows = Stream.of(
                        defaultInlineKeyboard,
                        mergeWithCategoriesKeyboard,
                        mergeWithCategoriesAndTransactionsKeyboard)
                .map(InlineKeyboardRow::new)
                .toList();

        return new InlineKeyboardMarkup(rows);
    }
}
