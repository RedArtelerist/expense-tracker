package com.redartis.tg.repository;

import com.redartis.tg.model.MergeRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeRequestRepository extends MongoRepository<MergeRequest, Long> {
    @Query("{ 'chatId' : ?0, 'completed' : false }")
    MergeRequest getMergeRequestByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
