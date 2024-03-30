package com.redartis.expense.repository;

import com.redartis.dto.constants.Type;
import com.redartis.expense.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths = "keywords")
    Optional<Category> findById(Long id);

    @Query("""
            SELECT c FROM Category c LEFT JOIN FETCH c.keywords
            WHERE c.account.id = :accountId AND c.type = :type""")
    List<Category> findAllByTypeAndAccountId(Long accountId, Type type);
}
