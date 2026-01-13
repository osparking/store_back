package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.model.FollowUp;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.question.QuestionRow;
import com.bumsoap.store.request.FollowUpData;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;

public interface QuestionServI {
    @Transactional
    FollowUp handleSaveFollowUp(FollowUpData followUpData);

    SearchResult<QuestionTableRowAdmin> getQuestionsPage(
            Integer page, Integer pageSize, Long userId);

    @Transactional
    Question handleSaveQuestion(QuestionSaveReq question);

    QuestionRow findById(Long id);
}
