package com.redartis.expense.service;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.category.MergeCategoryDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.config.properties.DefaultCategoryProperties;
import com.redartis.expense.exception.CategoryNameIsNotUniqueException;
import com.redartis.expense.exception.CategoryNotFoundException;
import com.redartis.expense.mapper.CategoryMapper;
import com.redartis.expense.model.Account;
import com.redartis.expense.model.Category;
import com.redartis.expense.repository.CategoryRepository;
import com.redartis.expense.repository.KeywordRepository;
import com.redartis.expense.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final DefaultCategoryProperties defaultCategoryProperties;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AccountService accountService;
    private final KeywordRepository keywordRepository;
    private final TransactionRepository transactionRepository;

    public List<CategoryDto> findCategoriesByUserId(Long userId) {
        return accountService.getAccountByUserIdWithCategories(userId)
                .getCategories()
                .stream()
                .map(categoryMapper::mapToDto)
                .sorted()
                .toList();
    }

    public List<CategoryDto> findCategoriesByChatId(Long chatId) {
        return accountService.getAccountByChatIdWithCategories(chatId)
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

    public Category getCategoryByIdWithAccount(Long categoryId) {
        return categoryRepository.findByIdWithAccount(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("Can't find category by id: " + categoryId)
        );
    }

    public CategoryDto findCategoryById(Long categoryId) {
        return categoryMapper.mapToDto(getCategoryById(categoryId));
    }

    public void setDefaultCategories(Long userId) {
        Account account = accountService.getAccountByUserId(userId);
        defaultCategoryProperties.getCategories()
                .forEach(category -> categoryRepository.save(new Category(
                        category.getName(),
                        category.getType(),
                        account
                )));
    }

    public void createCategory(Long userId, CategoryDto categoryDto) {
        try {
            Account account = accountService.getAccountByUserIdWithCategories(userId);
            Category category = categoryMapper.mapToCategory(categoryDto, account);
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryNameIsNotUniqueException(
                    String.format("Category name \"%s\" isn't unique!", categoryDto.name())
            );
        }
    }

    public void saveAllCategories(Set<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    public void updateCategory(Long userId, CategoryDto categoryDto) {
        Account account = accountService.getAccountByUserIdWithCategories(userId);
        Category updatedCategory = categoryMapper.mapToCategory(categoryDto, account);
        updatedCategory.setId(categoryDto.id());
        categoryRepository.save(updatedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void mergeCategory(MergeCategoryDto mergeCategoryDto) {
        Long categoryToMergeId = mergeCategoryDto.categoryToMergeId();
        Long categoryToChangeId = mergeCategoryDto.categoryToChangeId();
        keywordRepository.updateCategoryId(categoryToMergeId, categoryToChangeId);
        transactionRepository.updateCategoryId(categoryToMergeId, categoryToChangeId);
        categoryRepository.deleteById(categoryToMergeId);
    }

    public Optional<CategoryDto> findCategoryByNameFromList(List<CategoryDto> categories,
                                                            String categoryName) {
        return categories.stream()
                .filter(category -> categoryName.equals(category.name()))
                .findFirst();
    }
}
