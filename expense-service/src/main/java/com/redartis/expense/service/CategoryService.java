package com.redartis.expense.service;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.config.properties.DefaultCategoryProperties;
import com.redartis.expense.exception.CategoryNameIsNotUniqueException;
import com.redartis.expense.exception.CategoryNotFoundException;
import com.redartis.expense.mapper.CategoryMapper;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Category;
import com.redartis.expense.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AccountService accountService;
    private final CategoryMapper categoryMapper;
    private final DefaultCategoryProperties defaultCategoryProperties;

    public List<CategoryDto> findCategoriesByUserId(Long userId) {
        return accountService.getAccountByUserId(userId)
                .getCategories()
                .stream()
                .map(categoryMapper::mapToDto)
                .sorted()
                .toList();
    }

    public List<CategoryDto> findCategoriesByChatId(Long chatId) {
        return accountService.getAccountByChatId(chatId)
                .getCategories()
                .stream()
                .map(categoryMapper::mapToDto)
                .sorted()
                .toList();
    }

    public List<CategoryDto> findCategoriesByType(Long accountId, Type type) {
        Account account = accountService.getAccountByUserId(accountId);
        return categoryRepository.findAllByTypeAndAccountId(account.getId(), type)
                .stream()
                .map(categoryMapper::mapToDto)
                .sorted()
                .toList();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("Can't find category by id: " + categoryId)
        );
    }

    public CategoryDto findCategoryById(Long categoryId) {
        return categoryMapper.mapToDto(getCategoryById(categoryId));
    }

    public void setDefaultCategories(Long accountId) {
        Account account = accountService.getAccountByUserId(accountId);
        defaultCategoryProperties.getCategories()
                .forEach(category -> categoryRepository.save(new Category(
                        category.getName(),
                        category.getType(),
                        account
                )));
    }

    public void createCategory(Long accountId, CategoryDto categoryDto) {
        try {
            Account account = accountService.getAccountByUserId(accountId);
            Category category = categoryMapper.mapToCategory(categoryDto, account);
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryNameIsNotUniqueException(
                    String.format("Category name \"%s\" isn't unique!", categoryDto.name())
            );
        }
    }

    public void updateCategory(Long accountId, CategoryDto categoryDto) {
        Account account = accountService.getAccountByUserId(accountId);
        Category updatedCategory = categoryMapper.mapToCategory(categoryDto, account);
        updatedCategory.setId(categoryDto.id());
        categoryRepository.save(updatedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
