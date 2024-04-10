package com.redartis.recognizerservice.controller;

import com.redartis.dto.category.CategoryDto;
import com.redartis.recognizerservice.service.CategoryRecognizerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecognizerController {
    private final CategoryRecognizerService categoryRecognizerService;

    @GetMapping("/recognizer")
    public String getRecognizer() {
        log.info("GET request on /recognizer, test");
        return "Recognizer";
    }

    @PostMapping("/recognizer/category/suggest")
    public void recognizeCategory(@RequestParam(name = "message") String message,
                                  @RequestParam(name = "transactionId") UUID transactionId,
                                  @RequestBody List<CategoryDto> categories) {
        categoryRecognizerService.sendTransactionWithSuggestedCategory(
                message,
                categories,
                transactionId
        );
    }
}
