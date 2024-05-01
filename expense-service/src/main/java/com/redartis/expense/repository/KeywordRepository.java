package com.redartis.expense.repository;

import com.redartis.expense.model.Keyword;
import com.redartis.expense.model.KeywordId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, KeywordId> {
    Optional<Keyword> findByKeywordId(KeywordId keywordId);

    @Query("SELECT k FROM Keyword k WHERE k.keywordId.accountId = :accountId")
    List<Keyword> findAllByAccount(Long accountId);

    @Modifying
    @Query("""
            UPDATE Keyword k SET k.category.id = :newCategoryId
            WHERE k.category.id= :oldCategoryId""")
    void updateCategoryId(Long oldCategoryId, Long newCategoryId);

    @Modifying
    @Query("UPDATE Keyword k SET k.category.id = null WHERE k.keywordId = :keywordId")
    void removeCategoryId(KeywordId keywordId);

    void deleteByKeywordId(KeywordId keywordId);

    @Modifying
    @Query("DELETE FROM Keyword k WHERE k.keywordId.accountId = :accountId")
    void deleteAllByKeywordId_AccountId(Long accountId);

    @Modifying
    @Query("""
            UPDATE Keyword c SET c.keywordId.accountId = :newAccountId
            WHERE c.keywordId.accountId = :oldAccountId""")
    void updateAccountId(Long oldAccountId, Long newAccountId);

}
