package com.redartis.expense.repository;

import com.redartis.expense.model.Transaction;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :id")
    Page<Transaction> findAllByAccountId(Long id, Pageable pageable);

    @Query("""
            SELECT t FROM Transaction t
            WHERE t.account.id = :accountId AND t.category.id is null
            ORDER BY t.date DESC"""
    )
    List<Transaction> findAllWithoutCategoriesByAccountId(Long accountId);

    @Query("""
            SELECT t FROM Transaction t
            WHERE MONTH(t.date) = :month AND YEAR(t.date) = :year AND t.category.id = :categoryId
            ORDER BY t.date"""
    )
    List<Transaction> findTransactionsBetweenDatesAndCategory(
            Integer year,
            Integer month,
            long categoryId
    );
}
