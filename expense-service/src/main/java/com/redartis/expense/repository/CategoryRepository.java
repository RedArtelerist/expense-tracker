package com.redartis.expense.repository;

import com.redartis.dto.analytics.AnalyticsDataDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths = "keywords")
    Optional<Category> findById(Long id);

    @Query("SELECT c FROM Category c JOIN FETCH c.account WHERE c.id = :id")
    Optional<Category> findByIdWithAccount(Long id);

    @Query("""
            SELECT c FROM Category c LEFT JOIN FETCH c.keywords
            WHERE c.account.id = :accountId AND c.type = :type""")
    List<Category> findAllByTypeAndAccountId(Long accountId, Type type);

    @Modifying
    @Query("UPDATE Category c SET c.account.id = :newAccountId WHERE c.account.id = :oldAccountId")
    void updateAccountId(Long oldAccountId, Long newAccountId);

    @Query("SELECT c FROM Category c WHERE c.account.id = :accountId AND c.name = :name")
    Category findCategoryByNameAndAccountId(Long accountId, String name);

    @Transactional
    void deleteAllByAccountId(Long accountId);

    @Query("""
            SELECT new com.redartis.dto.analytics.AnalyticsDataDto(
                c.id,
                c.name,
                (SUM(t.amount)) / (
                    EXTRACT(MONTH FROM MAX(t.date)) - EXTRACT(MONTH FROM MIN(t.date)) +
                    ((EXTRACT(YEAR FROM MAX(t.date)) - EXTRACT(YEAR FROM MIN(t.date))) * 12) + 1
                )
            )
            FROM Category c LEFT JOIN FETCH Transaction t ON t.category.id = c.id
            WHERE c.account.id = :accountId AND c.type = :type
            GROUP BY c.id""")
    List<AnalyticsDataDto> findMediumAmountOfAllCategoriesByAccountIdAndType(
            Long accountId,
            Type type
    );
}
