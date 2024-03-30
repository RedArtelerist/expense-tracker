package com.redartis.expense.controller.rest;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.service.CategoryService;
import com.redartis.expense.util.TelegramUtils;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final TelegramUtils telegramUtils;
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(Principal principal) {
        return categoryService.findCategoriesByUserId(telegramUtils.getTelegramId(principal));
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable("id") Long id) {
        return categoryService.findCategoryById(id);
    }

    @GetMapping("/types/{type}")
    public List<CategoryDto> getCategoryByType(
            Principal principal,
            @PathVariable("type") Type type) {
        return categoryService.findCategoriesByType(telegramUtils.getTelegramId(principal), type);
    }

    @PostMapping("/add-default-categories")
    public void addDefaultCategories(Principal principal) {
        categoryService.setDefaultCategories(telegramUtils.getTelegramId(principal));
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> createCategory(
            Principal principal,
            @RequestBody CategoryDto category) {
        categoryService.createCategory(telegramUtils.getTelegramId(principal), category);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<HttpStatus> updateCategory(
            Principal principal,
            @RequestBody CategoryDto category) {
        categoryService.updateCategory(telegramUtils.getTelegramId(principal), category);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(
            Principal principal,
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
