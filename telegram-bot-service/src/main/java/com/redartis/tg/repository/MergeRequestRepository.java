package com.redartis.tg.repository;

import com.redartis.tg.model.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
    @Query("SELECT mr FROM MergeRequest mr WHERE mr.chatId = :chatId AND mr.completed = false")
    MergeRequest getMergeRequestByChatId(Long chatId);

    @Modifying
    @Query("UPDATE MergeRequest mr SET mr.completed = true WHERE mr.chatId = :chatId")
    void updateMergeRequestCompletionByChatId(Long chatId);

    @Transactional
    void deleteByChatId(Long chatId);
}
