package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;

public interface QuestionServI {
    SearchResult<QuestionTableRowAdmin> getQuestionsPage(Integer page, Integer size);

    @Transactional
    Question handleSaveQuestion(QuestionSaveReq question);
}
