package com.redartis.expense.repository;

import com.redartis.dto.constants.Type;
import com.redartis.expense.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
