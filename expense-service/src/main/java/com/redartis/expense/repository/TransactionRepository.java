package com.redartis.expense.repository;

import com.redartis.dto.analytics.MonthlyAnalyticsByCategoryDto;
import com.redartis.dto.constants.Type;
import com.redartis.expense.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE t.id = :id")
    Optional<Transaction> findById(UUID id);

    @Query("""
            SELECT t FROM Transaction t
            LEFT JOIN FETCH t.category
            JOIN FETCH t.account
            WHERE t.id = :id""")
    Optional<Transaction> findByIdWithAccount(UUID id);

    @Query("""
            SELECT t FROM Transaction t
            LEFT JOIN FETCH t.category
            WHERE t.account.id = :accountId
            ORDER BY t.date DESC""")
    List<Transaction> findAllByAccountId(Long accountId);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE t.account.id = :id")
    Page<Transaction> findAllByAccountId(Long id, Pageable pageable);

    @Query("""
            SELECT t FROM Transaction t
            LEFT JOIN FETCH t.category
            WHERE t.account.id = :accountId AND t.telegramUserId = :userId
            ORDER BY t.date DESC""")
    List<Transaction> findAllByAccountIdAndUserId(Long accountId, Long userId);

    @Query("""
            SELECT t FROM Transaction t
            WHERE t.account.id = :accountId AND t.category.id is null
            ORDER BY t.date DESC""")
    List<Transaction> findAllWithoutCategoriesByAccountId(Long accountId);

    @Query("""
            SELECT t FROM Transaction t
            LEFT JOIN FETCH t.category
            WHERE MONTH(t.date) = :month AND YEAR(t.date) = :year
            AND t.category.name = :categoryName
            ORDER BY t.date""")
    List<Transaction> findTransactionsBetweenDatesAndCategory(
            Integer year,
            Integer month,
            String categoryName
    );

    @Modifying
    @Query("""
            UPDATE Transaction t SET t.account.id = :newAccountId
            WHERE t.account.id = :oldAccountId""")
    void updateAccountId(Long oldAccountId, Long newAccountId);

    @Modifying
    @Query("""
            UPDATE Transaction t SET t.category.id = :newCategoryId
            WHERE t.category.id = :oldCategoryId""")
    void updateCategoryId(Long oldCategoryId, Long newCategoryId);

    @Modifying
    @Query("""
            UPDATE Transaction t SET t.category.id = :newCategoryId
            WHERE t.account.id = :accountId AND t.category.id IS NULL AND t.message = :message""")
    void updateCategoryIdWhereCategoryIsNull(Long newCategoryId, String message, Long accountId);

    @Modifying
    @Query("""
            UPDATE Transaction t SET t.category.id = NULL
            WHERE t.account.id = :accountId AND t.message = :message""")
    void removeCategoryIdFromTransactionsWithSameMessage(String message, Long accountId);

    @Query("""
            SELECT DISTINCT EXTRACT(year from t.date) from Transaction t
            WHERE t.account.id = :accountId""")
    List<Integer> findAvailableYearsForAccountByAccountId(Long accountId);

    @Query("""
            SELECT new com.redartis.dto.analytics.MonthlyAnalyticsByCategoryDto(
                cast(SUM(t.amount) as bigdecimal),
                c.name,
                cast(MONTH(t.date) as int)
            )
            FROM Transaction t JOIN Category c ON t.category.id = c.id
            WHERE t.account.id = :accountId AND YEAR(t.date) = :year AND c.type = :type
            GROUP BY c.id, MONTH(t.date), c.name""")
    List<MonthlyAnalyticsByCategoryDto> findMonthlyStatisticsByYearAndAccountIdAndType(
            Long accountId,
            Integer year,
            Type type
    );

    @Query(value = """
            SELECT TRIM(to_char(t.date, 'Month')) AS month,
            SUM(CASE WHEN c.type = 'INCOME' THEN t.amount ELSE 0 END) AS totalIncome,
            SUM(CASE WHEN c.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS totalExpense
            FROM transactions t JOIN categories c ON t.category_id = c.id
            WHERE t.account_id = :accountId AND EXTRACT(YEAR FROM t.date) = :year
            GROUP BY month
            ORDER BY month""", nativeQuery = true)
    List<AnalyticsDataMonth> getTotalIncomeOutcomePerMonth(Long accountId, int year);

    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.account.id = :accountId")
    void deleteAllByAccountId(Long accountId);

    @Modifying
    void deleteAllByAccountIdAndTelegramUserId(Long accountId, Long telegramUserId);
}
