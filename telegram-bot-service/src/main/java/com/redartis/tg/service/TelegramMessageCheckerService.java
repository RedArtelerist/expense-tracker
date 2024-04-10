package com.redartis.tg.service;

import static org.apache.commons.lang.StringUtils.isNumeric;

import org.springframework.stereotype.Service;

@Service
public class TelegramMessageCheckerService {
    private boolean checkForTransactionalFormat(String[] words) {
        if (words.length == 0 || words.length == 1) {
            return false;
        }

        return isNumeric(words[0]) || isNumeric(words[words.length - 1]);
    }

    public boolean isNonTransactionalMessageMentioned(String message) {
        String[] words = message.split(" ");

        if (checkForTransactionalFormat(words)) {
            return false;
        }

        for (String word : words) {
            if (word.startsWith("@")) {
                return true;
            }
        }

        return false;
    }
}
