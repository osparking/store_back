package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;

import java.util.List;

public interface QuestionServI {
    List<QuestionTableRowAdmin> getAllQuestions();

    @Transactional
    Question handleSaveQuestion(QuestionSaveReq question);
}
