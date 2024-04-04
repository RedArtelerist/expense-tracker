package com.redartis.expense.repository;

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
            WHERE t.account.id = :accountId AND t.category.id is null""")
    List<Transaction> findAllWithoutCategoriesByAccountId(Long accountId);

    @Query("""
            SELECT t FROM Transaction t
            LEFT JOIN FETCH t.category
            WHERE MONTH(t.date) = :month AND YEAR(t.date) = :year AND t.category.id = :categoryId
            ORDER BY t.date""")
    List<Transaction> findTransactionsBetweenDatesAndCategory(
            Integer year,
            Integer month,
            long categoryId
    );

    @Query("""
            SELECT DISTINCT EXTRACT(year from t.date) from Transaction t
            WHERE t.account.id = :accountId""")
    List<Integer> findAvailableYearsForAccountByAccountId(Long accountId);

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
}
