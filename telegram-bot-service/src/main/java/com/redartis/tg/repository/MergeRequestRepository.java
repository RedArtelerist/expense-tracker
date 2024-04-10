package com.redartis.tg.repository;

import com.redartis.tg.model.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
    MergeRequest getMergeRequestByChatId(Long chatId);

    @Modifying
    @Query("UPDATE MergeRequest mr SET mr.completed = true WHERE mr.chatId = :chatId")
    void updateMergeRequestCompletionByChatId(Long chatId);
}
