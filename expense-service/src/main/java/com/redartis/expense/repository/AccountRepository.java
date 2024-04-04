package com.redartis.expense.repository;

import com.redartis.expense.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByChatId(Long chatId);

    @Query("""
            SELECT a FROM Account a
            LEFT JOIN FETCH a.categories c
            LEFT JOIN FETCH c.keywords
            WHERE a.chatId = :chatId""")
    Optional<Account> findByChatIdWithCategories(Long chatId);

    @Query("SELECT a FROM Account a JOIN FETCH a.users u WHERE u.id = :userId")
    Account findNewAccountByUserId(Long userId);

    @Query(value = """
            SELECT count(*) FROM(
                SELECT account_id FROM users
                GROUP BY account_id HAVING count(account_id) > 1) as t""",
            nativeQuery = true)
    int getGroupAccountsCount();
}
