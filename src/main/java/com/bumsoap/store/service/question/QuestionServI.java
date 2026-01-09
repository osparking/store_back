package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

public interface QuestionServI {
    Page<QuestionTableRowAdmin> getQuestionsPage(Integer page, Integer size);

    @Transactional
    Question handleSaveQuestion(QuestionSaveReq question);
}
