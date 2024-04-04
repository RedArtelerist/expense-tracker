package com.redartis.expense.mapper;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.category.KeywordIdDto;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Category;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
@Slf4j
public class CategoryMapper {
    public CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .keywords(getCategoryKeywords(category))
                .build();
    }

    public Category mapToCategory(CategoryDto categoryDto, Account account) {
        return Category.builder()
                .name(StringUtils.capitalize(categoryDto.name()))
                .type(categoryDto.type())
                .account(account)
                .build();
    }

    private List<KeywordIdDto> getCategoryKeywords(Category category) {
        return category.getKeywords()
                .stream()
                .map(keyword -> new KeywordIdDto(
                        keyword.getKeywordId().getAccountId(),
                        keyword.getKeywordId().getName())
                )
                .sorted(Comparator.comparing(KeywordIdDto::name))
                .toList();
    }
}
