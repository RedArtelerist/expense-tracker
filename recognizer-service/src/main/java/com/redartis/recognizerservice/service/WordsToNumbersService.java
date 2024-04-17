package com.redartis.recognizerservice.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class WordsToNumbersService {
    private static final String SPACE = " ";
    private static final String NUMBER_CONTAINS_AT_START_OR_END_REGEX =
            "^\\d.*|.*\\d$";
    private static final String NON_ALPHA_NUMERIC_REGEX = "[^a-zA-Zа-яіїА-ЯІЇ0-9\"\\s]";

    private final Map<String, Long> vocabulary = new HashMap<>();
    private final List<String> currencies = new ArrayList<>();

    @PostConstruct
    public void fillCurrencies() {
        currencies.add("гривня");
        currencies.add("гривні");
        currencies.add("гривень");
        currencies.add("гривені");
        currencies.add("гривинь");
    }

    @PostConstruct
    public void fillVocabulary() {
        vocabulary.put("ноль", 0L);
        vocabulary.put("один", 1L);
        vocabulary.put("одна", 1L);
        vocabulary.put("два", 2L);
        vocabulary.put("дві", 2L);
        vocabulary.put("три", 3L);
        vocabulary.put("чотири", 4L);
        vocabulary.put("п'ять", 5L);
        vocabulary.put("пять", 5L);
        vocabulary.put("шість", 6L);
        vocabulary.put("сім", 7L);
        vocabulary.put("вісім", 8L);
        vocabulary.put("дев'ять", 9L);
        vocabulary.put("девять", 9L);
        vocabulary.put("десять", 10L);
        vocabulary.put("одинадцять", 11L);
        vocabulary.put("дванадцять", 12L);
        vocabulary.put("тринадцять", 13L);
        vocabulary.put("чотирнадцять", 14L);
        vocabulary.put("п'ятнадцять", 15L);
        vocabulary.put("пятнадцять", 15L);
        vocabulary.put("шістнадцять", 16L);
        vocabulary.put("сімнадцять", 17L);
        vocabulary.put("вісімнадцять", 18L);
        vocabulary.put("дев'ятнадцять", 19L);
        vocabulary.put("девятнадцять", 19L);
        vocabulary.put("двадцять", 20L);
        vocabulary.put("тридцять", 30L);
        vocabulary.put("сорок", 40L);
        vocabulary.put("п'ятдесят", 50L);
        vocabulary.put("пятдесят", 50L);
        vocabulary.put("шістдесят", 60L);
        vocabulary.put("сімдесят", 70L);
        vocabulary.put("вісімдесят", 80L);
        vocabulary.put("дев'яносто", 90L);
        vocabulary.put("девяносто", 90L);
        vocabulary.put("сто", 100L);
        vocabulary.put("двісті", 200L);
        vocabulary.put("триста", 300L);
        vocabulary.put("чотириста", 400L);
        vocabulary.put("п'ятсот", 500L);
        vocabulary.put("пятсот", 500L);
        vocabulary.put("шістсот", 600L);
        vocabulary.put("сімсот", 700L);
        vocabulary.put("вісімсот", 800L);
        vocabulary.put("дев'ятсот", 900L);
        vocabulary.put("девятсот", 900L);
        vocabulary.put("тисяча", 1000L);
        vocabulary.put("тисячі", 1000L);
        vocabulary.put("тисяч", 1000L);
        vocabulary.put("тища", 1000L);
        vocabulary.put("тищу", 1000L);
        vocabulary.put("тищі", 1000L);
        vocabulary.put("мільйон", 1000000L);
        vocabulary.put("мільйона", 1000000L);
        vocabulary.put("мільйони", 1000000L);
        vocabulary.put("мільйонів", 1000000L);
    }

    public String processSpacesInNumbers(String input) {
        return input.replaceAll("(?<=\\d) +(?=\\d)", "");
    }

    public String wordsToNumbers(String text) {
        String words = processSpacesInNumbers(text);
        if (words.matches(NUMBER_CONTAINS_AT_START_OR_END_REGEX)) {
            return words;
        }

        String[] splitWords = words.replaceAll(NON_ALPHA_NUMERIC_REGEX, "").split("\\s+");
        StringBuilder message = new StringBuilder();
        long number = 0;
        long prevNumber = 0;

        for (String word : splitWords) {
            if (word.matches("\\d+")) {
                number += prevNumber;
                number += Long.parseLong(word);
                prevNumber = 0;
            } else if (vocabulary.containsKey(word.toLowerCase())) {
                long value = vocabulary.get(word.toLowerCase());
                if (value >= 1000) {
                    prevNumber = prevNumber == 0 ? 1 : prevNumber;
                    number += prevNumber * value;
                    prevNumber = 0;
                } else {
                    prevNumber += value;
                }
            } else if (!currencies.contains(word.toLowerCase())) {
                message.append(word).append(SPACE);
            }
        }
        number += prevNumber;
        message.append(number);
        return message.toString();
    }
}
