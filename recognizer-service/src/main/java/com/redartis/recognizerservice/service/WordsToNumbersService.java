package com.redartis.recognizerservice.service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class WordsToNumbersService {
    private static final String SPACE = " ";
    private static final String NON_ALPHA_NUMERIC_REGEX = "[^a-zA-Zа-яіїА-ЯІЇ0-9\",.\\s]";
    private static final String AMOUNT_REGEX = "\\d+|\\d+[,.]\\d{1,2}";

    private final Map<String, Long> vocabulary = new HashMap<>();
    private final List<String> currencies = new ArrayList<>();

    @PostConstruct
    public void fillCurrencies() {
        currencies.add("гривня");
        currencies.add("гривні");
        currencies.add("гривень");
        currencies.add("гривені");
        currencies.add("гривинь");
        currencies.add("грн");
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
        vocabulary.put("млн", 1000000L);
        vocabulary.put("мільйона", 1000000L);
        vocabulary.put("мільйони", 1000000L);
        vocabulary.put("мільйонів", 1000000L);
    }

    public String wordsToNumbers(String text) {
        String words = processSpacesInNumbers(text);
        String[] splitWords = words.replaceAll(NON_ALPHA_NUMERIC_REGEX, "").split("\\s+");
        StringBuilder message = new StringBuilder();

        BigDecimal number = BigDecimal.ZERO;
        BigDecimal prevNumber = BigDecimal.ZERO;

        for (String word : splitWords) {
            String processedWord = processWord(word);
            if (word.matches(AMOUNT_REGEX)) {
                BigDecimal value = new BigDecimal(word.replace(",", "."));
                prevNumber = prevNumber.add(value);
            } else if (vocabulary.containsKey(processedWord)) {
                long value = vocabulary.get(processedWord);
                if (value >= 1000) {
                    prevNumber = Objects.equals(prevNumber, BigDecimal.ZERO)
                            ? BigDecimal.ONE : prevNumber;
                    number = number.add(prevNumber.multiply(BigDecimal.valueOf(value)));
                    prevNumber = BigDecimal.ZERO;
                } else {
                    prevNumber = prevNumber.add(BigDecimal.valueOf(value));
                }
            } else if (!currencies.contains(processedWord)) {
                message.append(processedWord).append(SPACE);
            }
        }

        number = number.add(prevNumber);
        if (!number.equals(BigDecimal.ZERO)) {
            message.append(number.toPlainString());
        }
        return message.toString();
    }

    private String processSpacesInNumbers(String input) {
        return input.replaceAll("(?<=\\d) +(?=\\d)", "");
    }

    private String processWord(String word) {
        return word.toLowerCase().replaceAll("[,.]", "");
    }
}
