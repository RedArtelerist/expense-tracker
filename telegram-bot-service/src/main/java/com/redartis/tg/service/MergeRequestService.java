package com.redartis.tg.service;

import com.redartis.tg.mapper.MergeRequestMapper;
import com.redartis.tg.model.MergeRequest;
import com.redartis.tg.repository.MergeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Service
@RequiredArgsConstructor
public class MergeRequestService {
    private final MergeRequestRepository mergeRequestRepository;
    private final MergeRequestMapper mergeRequestMapper;
    private final MongoTemplate mongoTemplate;

    public void saveMergeRequestMessage(Message message) {
        saveMergeRequest(mergeRequestMapper.mapMergeRequestMessageToMergeRequest(message));
    }

    public void saveMergeRequest(MergeRequest mergeRequest) {
        mergeRequestRepository.save(mergeRequest);
    }

    public MergeRequest getMergeRequestByChatId(Long chatId) {
        return mergeRequestRepository.getMergeRequestByChatId(chatId);
    }

    public void updateMergeRequestCompletionByChatId(Long chatId) {
        Query query = new Query().addCriteria(Criteria.where("chatId").is(chatId));
        Update update = new Update().set("completed", true);
        mongoTemplate.updateFirst(query, update, MergeRequest.class);
    }

    public void deleteMergeRequestsByChatId(Long chatId) {
        mergeRequestRepository.deleteByChatId(chatId);
    }
}
