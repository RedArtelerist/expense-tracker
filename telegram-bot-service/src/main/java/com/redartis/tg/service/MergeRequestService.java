package com.redartis.tg.service;

import com.redartis.tg.mapper.MergeRequestMapper;
import com.redartis.tg.model.MergeRequest;
import com.redartis.tg.repository.MergeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Service
@RequiredArgsConstructor
public class MergeRequestService {
    private final MergeRequestRepository mergeRequestRepository;
    private final MergeRequestMapper mergeRequestMapper;

    public void saveMergeRequestMessage(Message message) {
        saveMergeRequest(mergeRequestMapper.mapMergeRequestMessageToMergeRequest(message));
    }

    public void saveMergeRequest(MergeRequest mergeRequest) {
        mergeRequestRepository.save(mergeRequest);
    }

    public MergeRequest getMergeRequestByChatId(Long chatId) {
        return mergeRequestRepository.getMergeRequestByChatId(chatId);
    }

    @Transactional
    public void updateMergeRequestCompletionByChatId(Long chatId) {
        mergeRequestRepository.updateMergeRequestCompletionByChatId(chatId);
    }

    public void deleteMergeRequestsByChatId(Long chatId) {
        mergeRequestRepository.deleteByChatId(chatId);
    }
}
