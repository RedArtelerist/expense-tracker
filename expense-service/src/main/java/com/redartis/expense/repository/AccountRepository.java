package com.redartis.expense.repository;

import com.redartis.expense.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByChatId(Long chatId);
}
