package com.bumsoap.store.service.question;

import com.bumsoap.store.model.Question;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;

public interface QuestionServI {
    @Transactional
    Question handleSaveQuestion(QuestionSaveReq question);
}
