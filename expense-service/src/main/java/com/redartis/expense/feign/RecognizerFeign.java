package com.redartis.expense.feign;

import com.redartis.dto.category.CategoryDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "recognizer-service")
public interface RecognizerFeign {
    @PostMapping("/recognizer/category/suggest")
    CategoryDto recognizeCategory(@RequestParam String message,
                                  @RequestParam UUID transactionId,
                                  @RequestBody List<CategoryDto> categories);
}
